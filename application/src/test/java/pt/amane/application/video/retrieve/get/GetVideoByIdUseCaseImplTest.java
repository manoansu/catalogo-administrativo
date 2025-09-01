package pt.amane.application.video.retrieve.get;

import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoMediaType;

class GetVideoByIdUseCaseImplTest extends UseCaseTest {

  @InjectMocks
  private GetVideoByIdUseCaseImpl useCase;
  
  @Mock
  private VideoGateway videoGateway;
  
  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway);
  }

  @Test
  void givenAValidId_whenCallsGetVideo_shouldReturnIt() {

    // given
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
    final var expectedVideo = FixtureUtils.Videos.audioVideo(VideoMediaType.VIDEO);
    final var expectedTrailer = FixtureUtils.Videos.audioVideo(VideoMediaType.TRAILER);
    final var expectedBanner = FixtureUtils.Videos.image(VideoMediaType.BANNER);
    final var expectedThumb = FixtureUtils.Videos.image(VideoMediaType.THUMBNAIL);
    final var expectedThumbHalf = FixtureUtils.Videos.image(VideoMediaType.THUMBNAIL_HALF);

    final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers
        )
        .updateVideoMedia(expectedVideo)
        .updateTrailerMedia(expectedTrailer)
        .updateBannerMedia(expectedBanner)
        .updateThumbnailMedia(expectedThumb)
        .updateThumbnailHalfMedia(expectedThumbHalf);

    final var expectedId = aVideo.getId();

    Mockito.when(videoGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(Video.with(aVideo)));

    // when
    final var actualVideo = this.useCase.execute(expectedId.getValue());

    // then
    Assertions.assertEquals(expectedId.getValue(), actualVideo.id());
    Assertions.assertEquals(expectedTitle, actualVideo.title());
    Assertions.assertEquals(expectedDescription, actualVideo.description());
    Assertions.assertEquals(expectedLaunchYear.getValue(), actualVideo.launchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.duration());
    Assertions.assertEquals(expectedOpened, actualVideo.opened());
    Assertions.assertEquals(expectedPublished, actualVideo.published());
    Assertions.assertEquals(expectedRating, actualVideo.rating());
    Assertions.assertEquals(asString(expectedCategories), actualVideo.categories());
    Assertions.assertEquals(asString(expectedGenres), actualVideo.genres());
    Assertions.assertEquals(asString(expectedMembers), actualVideo.castMembers());
    Assertions.assertEquals(expectedVideo, actualVideo.video());
    Assertions.assertEquals(expectedTrailer, actualVideo.trailer());
    Assertions.assertEquals(expectedBanner, actualVideo.banner());
    Assertions.assertEquals(expectedThumb, actualVideo.thumbnail());
    Assertions.assertEquals(expectedThumbHalf, actualVideo.thumbnailHalf());
    Assertions.assertEquals(aVideo.getCreatedAt(), actualVideo.createdAt());
    Assertions.assertEquals(aVideo.getUpdatedAt(), actualVideo.updatedAt());
  }

  @Test
  void givenInvalidId_whenCallsGetVideo_shouldReturnNotFound() {

    // given
    final var expectedErrorMessage = "Video with ID 123 was not found";

    final var expectedId = VideoID.from("123");

    Mockito.when(videoGateway.findById(Mockito.any()))
        .thenReturn(Optional.empty());

    // when
    final var actualError = Assertions.assertThrows(
        NotFoundException.class,
        () -> this.useCase.execute(expectedId.getValue())
    );

    // then
    Assertions.assertEquals(expectedErrorMessage, actualError.getMessage());
  }

}