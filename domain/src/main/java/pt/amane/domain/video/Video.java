package pt.amane.domain.video;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import pt.amane.AggregateRoot;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.event.DomainEvent;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.utils.InstantUtils;
import pt.amane.domain.validation.ValidationHandler;

public class Video extends AggregateRoot<VideoID> {

  private String title;
  private String description;
  private Year launchedAt;
  private double duration;
  private Rating rating;

  private boolean opened;
  private boolean published;

  private Instant createdAt;
  private Instant updatedAt;

  private ImageMedia banner;
  private ImageMedia thumbnail;
  private ImageMedia thumbnailHalf;

  private AudioVideoMedia trailer;
  private AudioVideoMedia video;

  private Set<CategoryID> categoires;
  private Set<GenreID> genres;
  private Set<CastMemberID> castMembers;


  protected Video(
    final VideoID videoID,
    final String title,
    final String description,
    final Year launchedAt,
    final double duration,
    final boolean opened,
    final boolean published,
    final Rating rating,
    final Instant createdAt,
    final Instant updatedAt,
    final ImageMedia banner,
    final ImageMedia thumbnail,
    final ImageMedia thumbnailHalf,
    final AudioVideoMedia trailer,
    final AudioVideoMedia video,
    final Set<CategoryID> categoires,
    final Set<GenreID> genres,
    final Set<CastMemberID> castMembers,
    final List<DomainEvent> events
  ) {
    super(videoID, events);
    this.title = title;
    this.description = description;
    this.launchedAt = launchedAt;
    this.duration = duration;
    this.opened = opened;
    this.published = published;
    this.rating = rating;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.banner = banner;
    this.thumbnail = thumbnail;
    this.thumbnailHalf = thumbnailHalf;
    this.trailer = trailer;
    this.video = video;
    this.categoires = categoires;
    this.genres = genres;
    this.castMembers = castMembers;
  }

  @Override
  public void validate(ValidationHandler handler) {
    new VideoValidator(this, handler).validate();
  }

  public Video update(
      final String aTitle,
      final String aDescription,
      final Year aLaunchYear,
      final double aDuration,
      final boolean wasOpened,
      final boolean wasPublished,
      final Rating aRating,
      final Set<CategoryID> categories,
      final Set<GenreID> genres,
      final Set<CastMemberID> members
  ) {
    this.title = aTitle;
    this.description = aDescription;
    this.launchedAt = aLaunchYear;
    this.duration = aDuration;
    this.rating = aRating;
    this.opened = wasOpened;
    this.published = wasPublished;
    this.setCategories(categories);
    this.setGenres(genres);
    this.setCastMembers(members);
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public Year getLaunchedAt() {
    return launchedAt;
  }

  public void setLaunchedAt(final Year launchedAt) {
    this.launchedAt = launchedAt;
  }

  public double getDuration() {
    return duration;
  }

  public void setDuration(final double duration) {
    this.duration = duration;
  }

  public Rating getRating() {
    return rating;
  }

  public void setRating(final Rating rating) {
    this.rating = rating;
  }

  public boolean getOpened() {
    return opened;
  }

  public void setOpened(final boolean opened) {
    this.opened = opened;
  }

  public boolean getPublished() {
    return published;
  }

  public void setPublished(final boolean published) {
    this.published = published;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(final Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(final Instant updatedAt) {
    this.updatedAt = updatedAt;
  }

  /**
   * avoid null pointer exception, because return value could be null
   */
  public Optional<ImageMedia> getBanner() {
    return Optional.ofNullable(banner);
  }

  public void setBanner(final ImageMedia banner) {
    this.banner = banner;
  }

  /**
   * avoid null pointer exception, because return value could be null
   */
  public Optional<ImageMedia> getThumbnail() {
    return Optional.ofNullable(thumbnail);
  }

  public void setThumbnail(final ImageMedia thumbnail) {
    this.thumbnail = thumbnail;
  }

  /**
   * avoid null pointer exception, because return value could be null
   */
  public Optional<ImageMedia> getThumbnailHalf() {
    return Optional.ofNullable(thumbnailHalf);
  }

  public void setThumbnailHalf(final ImageMedia thumbnailHalf) {
    this.thumbnailHalf = thumbnailHalf;
  }

  /**
   * avoid null pointer exception, because return value could be null
   */
  public Optional<AudioVideoMedia> getTrailer() {
    return Optional.ofNullable(trailer);
  }

  public void setTrailer(final AudioVideoMedia trailer) {
    this.trailer = trailer;
  }

  /**
  * avoid null pointer exception, because return value could be null
   */
  public Optional<AudioVideoMedia> getVideo() {
    return Optional.ofNullable(video);
  }

  public void setVideo(final AudioVideoMedia video) {
    this.video = video;
  }

  /**
   * We guarantee that the collection will not return null or cannot be modified.
   */
  public Set<CategoryID> getCategories() {
    return categoires != null ? Collections.unmodifiableSet(categoires) : Collections.emptySet();
  }

  public Set<GenreID> getGenres() {
    return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
  }

  public Set<CastMemberID> getCastMembers() {
    return castMembers != null ? Collections.unmodifiableSet(castMembers) : Collections.emptySet();
  }

  public Video updateBannerMedia(final ImageMedia banner) {
    this.banner = banner;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Video updateThumbnailMedia(final ImageMedia thumbnail) {
    this.thumbnail = thumbnail;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Video updateThumbnailHalfMedia(final ImageMedia thumbnailHalf) {
    this.thumbnailHalf = thumbnailHalf;
    this.updatedAt = InstantUtils.now();
    return this;
  }

  public Video updateTrailerMedia(final AudioVideoMedia trailer) {
    this.trailer = trailer;
    this.updatedAt = InstantUtils.now();
    onAudioVideoMediaUpdated(trailer);
    return this;
  }

  public Video updateVideoMedia(final AudioVideoMedia video) {
    this.video = video;
    this.updatedAt = InstantUtils.now();
    onAudioVideoMediaUpdated(trailer);
    return this;
  }

  public static Video newVideo(
      final String aTitle,
      final String aDescription,
      final Year aLaunchYear,
      final double aDuration,
      final boolean wasOpened,
      final boolean wasPublished,
      final Rating aRating,
      final Set<CategoryID> categories,
      final Set<GenreID> genres,
      final Set<CastMemberID> members
  ) {
    final var now = InstantUtils.now();
    final var anId = VideoID.unique();
    return new Video(
        anId,
        aTitle,
        aDescription,
        aLaunchYear,
        aDuration,
        wasOpened,
        wasPublished,
        aRating,
        now,
        now,
        null,
        null,
        null,
        null,
        null,
        categories,
        genres,
        members,
        null
    );
  }

  public static Video with(
      final VideoID anId,
      final String aTitle,
      final String aDescription,
      final Year aLaunchYear,
      final double aDuration,
      final boolean wasOpened,
      final boolean wasPublished,
      final Rating aRating,
      final Instant aCreationDate,
      final Instant aUpdateDate,
      final ImageMedia aBanner,
      final ImageMedia aThumb,
      final ImageMedia aThumbHalf,
      final AudioVideoMedia aTrailer,
      final AudioVideoMedia aVideo,
      final Set<CategoryID> categories,
      final Set<GenreID> genres,
      final Set<CastMemberID> members
  ) {
    return new Video(
        anId,
        aTitle,
        aDescription,
        aLaunchYear,
        aDuration,
        wasOpened,
        wasPublished,
        aRating,
        aCreationDate,
        aUpdateDate,
        aBanner,
        aThumb,
        aThumbHalf,
        aTrailer,
        aVideo,
        categories,
        genres,
        members,
        null
    );
  }

  public static Video with(final Video aVideo) {
    return new Video(
        aVideo.getId(),
        aVideo.getTitle(),
        aVideo.getDescription(),
        aVideo.getLaunchedAt(),
        aVideo.getDuration(),
        aVideo.getOpened(),
        aVideo.getPublished(),
        aVideo.getRating(),
        aVideo.getCreatedAt(),
        aVideo.getUpdatedAt(),
        aVideo.getBanner().orElse(null),
        aVideo.getThumbnail().orElse(null),
        aVideo.getThumbnailHalf().orElse(null),
        aVideo.getTrailer().orElse(null),
        aVideo.getVideo().orElse(null),
        new HashSet<>(aVideo.getCategories()),
        new HashSet<>(aVideo.getGenres()),
        new HashSet<>(aVideo.getCastMembers()),
        aVideo.getDomainEvents()
    );
  }

  private void setCastMembers(final Set<CastMemberID> members) {
    this.castMembers = members != null ? new HashSet<>(members) : Collections.emptySet();
  }

  private void setGenres(final Set<GenreID> genres) {
    this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
  }

  private void setCategories(final Set<CategoryID> categories) {
    this.categoires = categories != null ? new HashSet<>(categories) : Collections.emptySet();
  }

  private void onAudioVideoMediaUpdated(final AudioVideoMedia media) {
    if (media != null && media.isPendingEncode()) {
      this.registerDomainEvent(new VideoMediaCreated(getId().getValue(), media.rawLocation()));
    }
  }

}
