package pt.amane.application.video.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.application.video.create.MediaResourceGateway;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.DomainException;
import pt.amane.domain.exception.InternalErrorException;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.domain.video.Resource;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoMediaType;
import pt.amane.domain.video.VideoResource;

class UpdateVideoUseCaseImplTest extends UseCaseTest {


  @InjectMocks
  private UpdateVideoUseCaseImpl useCase;

  @Mock
  private VideoGateway videoGateway;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Mock
  private MediaResourceGateway mediaResourceGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway, categoryGateway, genreGateway, castMemberGateway, mediaResourceGateway);
  }

  @Test
  void givenAValidCommand_whenCallsUpdateVideo_shouldReturnVideoId() {

    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.of(
        FixtureUtils.CastMembers.wesley().getId(),
        FixtureUtils.CastMembers.gabriel().getId()
    );
    final Resource expectedVideo = FixtureUtils.Videos.resource(VideoMediaType.VIDEO);
    final Resource expectedTrailer = FixtureUtils.Videos.resource(VideoMediaType.TRAILER);
    final Resource expectedBanner = FixtureUtils.Videos.resource(VideoMediaType.BANNER);
    final Resource expectedThumb = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final Resource expectedThumbHalf = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(videoGateway).update(argThat(actualVideo ->
        Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPublished, actualVideo.getPublished())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedCategories, actualVideo.getCategories())
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
            && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
            && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
            && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
            && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
            && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
    ));
  }

  @Test
  void givenAValidCommandWithoutCategories_whenCallsUpdateVideo_shouldReturnVideoId() {

    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.of(
        FixtureUtils.CastMembers.wesley().getId(),
        FixtureUtils.CastMembers.gabriel().getId()
    );
    final Resource expectedVideo = FixtureUtils.Videos.resource(VideoMediaType.VIDEO);
    final Resource expectedTrailer = FixtureUtils.Videos.resource(VideoMediaType.TRAILER);
    final Resource expectedBanner = FixtureUtils.Videos.resource(VideoMediaType.BANNER);
    final Resource expectedThumb = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final Resource expectedThumbHalf = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(videoGateway).update(argThat(actualVideo ->
        Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPublished, actualVideo.getPublished())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedCategories, actualVideo.getCategories())
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
            && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
            && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
            && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
            && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
            && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
    ));
  }

  @Test
  void givenAValidCommandWithoutGenres_whenCallsUpdateVideo_shouldReturnVideoId() {

    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.of(
        FixtureUtils.CastMembers.wesley().getId(),
        FixtureUtils.CastMembers.gabriel().getId()
    );
    final Resource expectedVideo = FixtureUtils.Videos.resource(VideoMediaType.VIDEO);
    final Resource expectedTrailer = FixtureUtils.Videos.resource(VideoMediaType.TRAILER);
    final Resource expectedBanner = FixtureUtils.Videos.resource(VideoMediaType.BANNER);
    final Resource expectedThumb = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final Resource expectedThumbHalf = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(videoGateway).update(argThat(actualVideo ->
        Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPublished, actualVideo.getPublished())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedCategories, actualVideo.getCategories())
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
            && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
            && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
            && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
            && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
            && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
    ));
  }

  @Test
  void givenAValidCommandWithoutCastMembers_whenCallsUpdateVideo_shouldReturnVideoId() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = FixtureUtils.Videos.resource(VideoMediaType.VIDEO);
    final Resource expectedTrailer = FixtureUtils.Videos.resource(VideoMediaType.TRAILER);
    final Resource expectedBanner = FixtureUtils.Videos.resource(VideoMediaType.BANNER);
    final Resource expectedThumb = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final Resource expectedThumbHalf = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());


    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(videoGateway).update(argThat(actualVideo ->
        Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPublished, actualVideo.getPublished())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedCategories, actualVideo.getCategories())
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && Objects.equals(expectedVideo.name(), actualVideo.getVideo().get().name())
            && Objects.equals(expectedTrailer.name(), actualVideo.getTrailer().get().name())
            && Objects.equals(expectedBanner.name(), actualVideo.getBanner().get().name())
            && Objects.equals(expectedThumb.name(), actualVideo.getThumbnail().get().name())
            && Objects.equals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name())
            && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
            && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
    ));
  }

  @Test
  void givenAValidCommandWithoutResources_whenCallsUpdateVideo_shouldReturnVideoId() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.of(
        FixtureUtils.CastMembers.wesley().getId(),
        FixtureUtils.CastMembers.gabriel().getId()
    );
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualResult = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertNotNull(actualResult.id());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(videoGateway).update(argThat(actualVideo ->
        Objects.equals(expectedTitle, actualVideo.getTitle())
            && Objects.equals(expectedDescription, actualVideo.getDescription())
            && Objects.equals(expectedLaunchYear, actualVideo.getLaunchedAt())
            && Objects.equals(expectedDuration, actualVideo.getDuration())
            && Objects.equals(expectedOpened, actualVideo.getOpened())
            && Objects.equals(expectedPublished, actualVideo.getPublished())
            && Objects.equals(expectedRating, actualVideo.getRating())
            && Objects.equals(expectedCategories, actualVideo.getCategories())
            && Objects.equals(expectedGenres, actualVideo.getGenres())
            && Objects.equals(expectedMembers, actualVideo.getCastMembers())
            && actualVideo.getVideo().isEmpty()
            && actualVideo.getTrailer().isEmpty()
            && actualVideo.getBanner().isEmpty()
            && actualVideo.getThumbnail().isEmpty()
            && actualVideo.getThumbnailHalf().isEmpty()
            && Objects.equals(aVideo.getCreatedAt(), actualVideo.getCreatedAt())
            && aVideo.getUpdatedAt().isBefore(actualVideo.getUpdatedAt())
    ));
  }

  @Test
  void givenANullTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedErrorMessage = "'title' should not be null";
    final var expectedErrorCount = 1;

    final String expectedTitle = null;
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    // when
    final var actualException = Assertions.assertThrows(DomainException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(categoryGateway, times(0)).existsByIds(any());
    verify(castMemberGateway, times(0)).existsByIds(any());
    verify(genreGateway, times(0)).existsByIds(any());
    verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
    verify(videoGateway, times(0)).update(any());
  }

  @Test
  void givenAEmptyTitle_whenCallsUpdateVideo_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedErrorMessage = "'title' should not be empty";
    final var expectedErrorCount = 1;

    final String expectedTitle = " ";
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    // when
    final var actualException = Assertions.assertThrows(DomainException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(categoryGateway, times(0)).existsByIds(any());
    verify(castMemberGateway, times(0)).existsByIds(any());
    verify(genreGateway, times(0)).existsByIds(any());
    verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
    verify(videoGateway, times(0)).update(any());
  }

  @Test
  void givenANullRating_whenCallsUpdateVideo_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedErrorMessage = "'rating' should not be null";
    final var expectedErrorCount = 1;

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final String expectedRating = null;
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    // when
    final var actualException = Assertions.assertThrows(DomainException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(categoryGateway, times(0)).existsByIds(any());
    verify(castMemberGateway, times(0)).existsByIds(any());
    verify(genreGateway, times(0)).existsByIds(any());
    verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
    verify(videoGateway, times(0)).update(any());
  }

  @Test
  void givenAnInvalidRating_whenCallsUpdateVideo_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedErrorMessage = "'rating' should not be null";
    final var expectedErrorCount = 1;

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final String expectedRating = "ADASDA";
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    // when
    final var actualException = Assertions.assertThrows(DomainException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(categoryGateway, times(0)).existsByIds(any());
    verify(castMemberGateway, times(0)).existsByIds(any());
    verify(genreGateway, times(0)).existsByIds(any());
    verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
    verify(videoGateway, times(0)).update(any());
  }

  @Test
  void givenANullLaunchedAt_whenCallsUpdateVideo_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();

    final var expectedErrorMessage = "'launchedAt' should not be null";
    final var expectedErrorCount = 1;

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final Integer expectedLaunchYear = null;
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    // when
    final var actualException = Assertions.assertThrows(DomainException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(videoGateway).findById(eq(aVideo.getId()));

    verify(categoryGateway, times(0)).existsByIds(any());
    verify(castMemberGateway, times(0)).existsByIds(any());
    verify(genreGateway, times(0)).existsByIds(any());
    verify(mediaResourceGateway, times(0)).storeAudioVideo(any(), any());
    verify(videoGateway, times(0)).update(any());
  }

  @Test
  void givenAValidCommand_whenCallsUpdateVideoAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var aulasId = FixtureUtils.Categories.aulas().getId();

    final var expectedErrorMessage = "Some categories could not be found: %s".formatted(aulasId.getValue());
    final var expectedErrorCount = 1;

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = FixtureUtils.year();
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(aulasId);
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.of(FixtureUtils.CastMembers.wesley().getId());
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>());

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
    verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
    verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
    verify(mediaResourceGateway, times(0)).storeImage(any(), any());
    verify(videoGateway, times(0)).update(any());
  }

  @Test
  void givenAValidCommand_whenCallsUpdateVideoAndSomeGenresDoesNotExists_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var techId = FixtureUtils.Genres.tech().getId();

    final var expectedErrorMessage = "Some genres could not be found: %s".formatted(techId.getValue());
    final var expectedErrorCount = 1;

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = FixtureUtils.year();
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.of(techId);
    final var expectedMembers = Set.of(FixtureUtils.CastMembers.wesley().getId());
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>());

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
    verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
    verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
    verify(mediaResourceGateway, times(0)).storeImage(any(), any());
    verify(videoGateway, times(0)).create(any());
  }

  @Test
  void givenAValidCommand_whenCallsUpdateVideoAndSomeCastMembersDoesNotExists_shouldReturnDomainException() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var wesleyId = FixtureUtils.CastMembers.wesley().getId();

    final var expectedErrorMessage = "Some cast members could not be found: %s".formatted(wesleyId.getValue());
    final var expectedErrorCount = 1;

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = FixtureUtils.year();
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.of(wesleyId);
    final Resource expectedVideo = null;
    final Resource expectedTrailer = null;
    final Resource expectedBanner = null;
    final Resource expectedThumb = null;
    final Resource expectedThumbHalf = null;

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>());

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
    verify(castMemberGateway, times(1)).existsByIds(eq(expectedMembers));
    verify(genreGateway, times(1)).existsByIds(eq(expectedGenres));
    verify(mediaResourceGateway, times(0)).storeImage(any(), any());
    verify(videoGateway, times(0)).create(any());
  }

  @Test
  void givenAValidCommand_whenCallsCreateVideoThrowsException_shouldCallClearResources() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedErrorMessage = "An error on create video was observed [videoId:";

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(FixtureUtils.Categories.aulas().getId());
    final var expectedGenres = Set.of(FixtureUtils.Genres.tech().getId());
    final var expectedMembers = Set.of(
        FixtureUtils.CastMembers.wesley().getId(),
        FixtureUtils.CastMembers.gabriel().getId()
    );
    final Resource expectedVideo = FixtureUtils.Videos.resource(VideoMediaType.VIDEO);
    final Resource expectedTrailer = FixtureUtils.Videos.resource(VideoMediaType.TRAILER);
    final Resource expectedBanner = FixtureUtils.Videos.resource(VideoMediaType.BANNER);
    final Resource expectedThumb = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final Resource expectedThumbHalf = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL_HALF);

    final var aCommand = UpdateVideoCommand.with(
        aVideo.getId().getValue(),
        expectedTitle,
        expectedDescription,
        expectedLaunchYear.getValue(),
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating.getName(),
        asString(expectedCategories),
        asString(expectedGenres),
        asString(expectedMembers),
        expectedVideo,
        expectedTrailer,
        expectedBanner,
        expectedThumb,
        expectedThumbHalf
    );

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedCategories));

    when(castMemberGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedMembers));

    when(genreGateway.existsByIds(any()))
        .thenReturn(new ArrayList<>(expectedGenres));

    mockImageMedia();
    mockAudioVideoMedia();

    when(videoGateway.update(any()))
        .thenThrow(new RuntimeException("Internal Server Error"));

    // when
    final var actualResult = Assertions.assertThrows(InternalErrorException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualResult);
    Assertions.assertTrue(actualResult.getMessage().startsWith(expectedErrorMessage));

    verify(mediaResourceGateway, times(0)).clearResources(any());
  }

  private void mockImageMedia() {
    when(mediaResourceGateway.storeImage(any(), any())).thenAnswer(t -> {
      final var videoResource = t.getArgument(1, VideoResource.class);
      final var resource = videoResource.resource();
      return ImageMedia.with(resource.checksum(), resource.name(), "/img");
    });
  }

  private void mockAudioVideoMedia() {
    when(mediaResourceGateway.storeAudioVideo(any(), any())).thenAnswer(t -> {
      final var videoResource = t.getArgument(1, VideoResource.class);
      final var resource = videoResource.resource();
      return AudioVideoMedia.with(
          resource.checksum(),
          resource.name(),
          "/img"
      );
    });
  }
}
