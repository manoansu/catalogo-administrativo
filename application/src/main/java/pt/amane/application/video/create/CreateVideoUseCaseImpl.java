package pt.amane.application.video.create;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import pt.amane.Identifier;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.InternalErrorException;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.handler.Notification;
import pt.amane.domain.video.Rating;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoMediaType;
import pt.amane.domain.video.VideoResource;

public non-sealed class CreateVideoUseCaseImpl extends CreateVideoUseCase {

  private final CategoryGateway categoryGateway;
  private final CastMemberGateway castMemberGateway;
  private final GenreGateway genreGateway;
  private final MediaResourceGateway mediaResourceGateway;
  private final VideoGateway videoGateway;

  public CreateVideoUseCaseImpl(
      final CategoryGateway categoryGateway,
      final CastMemberGateway castMemberGateway,
      final GenreGateway genreGateway,
      final MediaResourceGateway mediaResourceGateway,
      final VideoGateway videoGateway
  ) {
    this.categoryGateway = (CategoryGateway) ObjectsValidator.objectValidation(categoryGateway);
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
    this.mediaResourceGateway = (MediaResourceGateway) ObjectsValidator.objectValidation(mediaResourceGateway);
    this.videoGateway = (VideoGateway) ObjectsValidator.objectValidation(videoGateway);
  }


  @Override
  public CreateVideoOutput execute(CreateVideoCommand aCommand) {
    final var aRating = Rating.of(aCommand.rating()).orElse(null);
    final var aLaunchYear = aCommand.launchedAt() != null ? Year.of(aCommand.launchedAt()) : null;
    final var categories = toIdentifier(aCommand.categories(), CategoryID::from);
    final var genres = toIdentifier(aCommand.genres(), GenreID::from);
    final var members = toIdentifier(aCommand.members(), CastMemberID::from);

    final var notification = Notification.create();
    notification.append(validateCategories(categories));
    notification.append(validateGenres(genres));
    notification.append(validateMembers(members));

    final var aVideo = Video.newVideo(
        aCommand.title(),
        aCommand.description(),
        aLaunchYear,
        aCommand.duration(),
        aCommand.opened(),
        aCommand.published(),
        aRating,
        categories,
        genres,
        members
    );

    aVideo.validate(notification);

    if (notification.hasErrors()) {
      throw new NotificationException("Could not create Aggregate Video", notification);
    }

    return CreateVideoOutput.from(create(aCommand, aVideo));
  }

  /**
   * Stored AudioVideo or Image of different Type of video.
   * @param aCommand
   * @param aVideo
   * @return
   */
  private Video create(CreateVideoCommand aCommand, Video aVideo) {
    final var anId = aVideo.getId();

    try {
      final var aVideoMedia = aCommand.getVideo()
          .map(obj -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(
              VideoMediaType.VIDEO, obj)))
          .orElse(null);

      final var aBannerMedia = aCommand.getBanner()
          .map(obj -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(
              VideoMediaType.BANNER, obj)))
          .orElse(null);

      final var aTraillerMedia = aCommand.getTrailer()
          .map(obj -> this.mediaResourceGateway.storeAudioVideo(anId, VideoResource.with(
              VideoMediaType.TRAILER, obj)))
          .orElse(null);

      final var aThumbnailMedia = aCommand.getThumbnail()
          .map(obj -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(
              VideoMediaType.THUMBNAIL, obj)))
          .orElse(null);

      final var aThumbnailHalfMedia = aCommand.getThumbnailHalf()
          .map(obj -> this.mediaResourceGateway.storeImage(anId, VideoResource.with(
              VideoMediaType.THUMBNAIL_HALF, obj)))
          .orElse(null);

      return this.videoGateway.create(
          aVideo
              .updateVideoMedia(aVideoMedia)
              .updateBannerMedia(aBannerMedia)
              .updateTrailerMedia(aTraillerMedia)
              .updateThumbnailMedia(aThumbnailMedia)
              .updateThumbnailHalfMedia(aThumbnailHalfMedia)
      );

    } catch (final Throwable t) {
      this.mediaResourceGateway.clearResources(anId); // clean the video resources stored before on google storage. Or Rollback.
      throw InternalErrorException.with(
          "An error on create video was observed [videoId:%s]".formatted(anId.getValue()),
          t
      );
    }
  }

  private ValidationHandler validateMembers(Set<CastMemberID> members) {
    return validateAggregate("Cast Members", members, castMemberGateway::existsByIds);
  }

  private ValidationHandler validateGenres(Set<GenreID> genres) {
    return validateAggregate("Genres", genres, genreGateway::existsByIds);
  }

  private ValidationHandler validateCategories(Set<CategoryID> categories) {
    return validateAggregate("Categories", categories, categoryGateway::existsByIds);
  }

  /**
   * Dinamic method to validate object aggregates like (CastMembers, Categories, Genres).
   * @param aggregateName
   * @param ids
   * @param existsByIds
   * @param <T>
   */
  private  <T extends Identifier> ValidationHandler validateAggregate(
      final String aggregateName,
      final Set<T> ids,
      final Function<Iterable<T> , List<T>> existsByIds
  ) {

    // Call an verify notification, if ids is null or empty return notification null.
    final var notification = Notification.create();
    if (ids == null || ids.isEmpty()) {
      return notification;
    }

    // retrieved ids, and verify  if the size Ids are different, remove all ids that isnt o db
    final var retrievedIds = existsByIds.apply(ids);
    if (retrievedIds.size() != ids.size()) {
      final var missingIds = new ArrayList<>(ids);
      missingIds.removeAll(retrievedIds);

      final var missingIdsString = missingIds.stream()
          .map(Identifier::getValue)
          .collect(Collectors.joining(", "));

      notification.append(new Error("Some %s could not be found: %s" .formatted(aggregateName, missingIdsString)));

    }
    return notification;
  }

//  private Supplier<DomainException> InvalidRating(final String rating) {
//    return () -> DomainException.with(new Error("Rating not found &s", formatted(rating));
//  }


  private <T> Set<T> toIdentifier(final Set<String> categories, final Function<String, T> mapper) {
    return categories.stream()
        .map(mapper)
        .collect(Collectors.toSet());
  }


}
