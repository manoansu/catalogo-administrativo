package pt.amane.application.video.media.upload;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.MediaResourceGateway;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoMediaType;
import pt.amane.domain.video.VideoResource;

class UploadMediaUseCaseImplTest extends UseCaseTest {

  @InjectMocks
  private UploadMediaUseCaseImpl useCase;

  @Mock
  private MediaResourceGateway mediaResourceGateway;

  @Mock
  private VideoGateway videoGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(mediaResourceGateway, videoGateway);
  }

  @Test
  void givenCmdToUpload_whenIsValid_shouldUpdateVideoMediaAndPersistIt() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedId = aVideo.getId();
    final var expectedType = VideoMediaType.VIDEO;
    final var expectedResource = FixtureUtils.Videos.resource(expectedType);
    final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(mediaResourceGateway.storeAudioVideo(any(), any()))
        .thenReturn(expectedMedia);

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

    // when
    final var actualOutput = useCase.execute(aCmd);

    // then
    Assertions.assertEquals(expectedType, actualOutput.mediaType());
    Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

    verify(videoGateway, times(1)).findById(eq(expectedId));

    verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

    verify(videoGateway, times(1)).update(argThat(actualVideo ->
        Objects.equals(expectedMedia, actualVideo.getVideo().get())
            && actualVideo.getTrailer().isEmpty()
            && actualVideo.getBanner().isEmpty()
            && actualVideo.getThumbnail().isEmpty()
            && actualVideo.getThumbnailHalf().isEmpty()
    ));
  }

  @Test
  void givenCmdToUpload_whenIsValid_shouldUpdateTrailerMediaAndPersistIt() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedId = aVideo.getId();
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedResource = FixtureUtils.Videos.resource(expectedType);
    final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(mediaResourceGateway.storeAudioVideo(any(), any()))
        .thenReturn(expectedMedia);

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

    // when
    final var actualOutput = useCase.execute(aCmd);

    // then
    Assertions.assertEquals(expectedType, actualOutput.mediaType());
    Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

    verify(videoGateway, times(1)).findById(eq(expectedId));

    verify(mediaResourceGateway, times(1)).storeAudioVideo(eq(expectedId), eq(expectedVideoResource));

    verify(videoGateway, times(1)).update(argThat(actualVideo ->
        actualVideo.getVideo().isEmpty()
            && Objects.equals(expectedMedia, actualVideo.getTrailer().get())
            && actualVideo.getBanner().isEmpty()
            && actualVideo.getThumbnail().isEmpty()
            && actualVideo.getThumbnailHalf().isEmpty()
    ));
  }

  @Test
  void givenCmdToUpload_whenIsValid_shouldUpdateBannerMediaAndPersistIt() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedId = aVideo.getId();
    final var expectedType = VideoMediaType.BANNER;
    final var expectedResource = FixtureUtils.Videos.resource(expectedType);
    final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
    final var expectedMedia = FixtureUtils.Videos.image(expectedType);

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(mediaResourceGateway.storeImage(any(), any()))
        .thenReturn(expectedMedia);

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

    // when
    final var actualOutput = useCase.execute(aCmd);

    // then
    Assertions.assertEquals(expectedType, actualOutput.mediaType());
    Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

    verify(videoGateway, times(1)).findById(eq(expectedId));

    verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

    verify(videoGateway, times(1)).update(argThat(actualVideo ->
        actualVideo.getVideo().isEmpty()
            && actualVideo.getTrailer().isEmpty()
            && Objects.equals(expectedMedia, actualVideo.getBanner().get())
            && actualVideo.getThumbnail().isEmpty()
            && actualVideo.getThumbnailHalf().isEmpty()
    ));
  }

  @Test
  void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailMediaAndPersistIt() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedId = aVideo.getId();
    final var expectedType = VideoMediaType.THUMBNAIL;
    final var expectedResource = FixtureUtils.Videos.resource(expectedType);
    final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
    final var expectedMedia = FixtureUtils.Videos.image(expectedType);

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(mediaResourceGateway.storeImage(any(), any()))
        .thenReturn(expectedMedia);

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

    // when
    final var actualOutput = useCase.execute(aCmd);

    // then
    Assertions.assertEquals(expectedType, actualOutput.mediaType());
    Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

    verify(videoGateway, times(1)).findById(eq(expectedId));

    verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

    verify(videoGateway, times(1)).update(argThat(actualVideo ->
        actualVideo.getVideo().isEmpty()
            && actualVideo.getTrailer().isEmpty()
            && actualVideo.getBanner().isEmpty()
            && Objects.equals(expectedMedia, actualVideo.getThumbnail().get())
            && actualVideo.getThumbnailHalf().isEmpty()
    ));
  }

  @Test
  void givenCmdToUpload_whenIsValid_shouldUpdateThumbnailHalfMediaAndPersistIt() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedId = aVideo.getId();
    final var expectedType = VideoMediaType.THUMBNAIL_HALF;
    final var expectedResource = FixtureUtils.Videos.resource(expectedType);
    final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);
    final var expectedMedia = FixtureUtils.Videos.image(expectedType);

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(mediaResourceGateway.storeImage(any(), any()))
        .thenReturn(expectedMedia);

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

    // when
    final var actualOutput = useCase.execute(aCmd);

    // then
    Assertions.assertEquals(expectedType, actualOutput.mediaType());
    Assertions.assertEquals(expectedId.getValue(), actualOutput.videoId());

    verify(videoGateway, times(1)).findById(eq(expectedId));

    verify(mediaResourceGateway, times(1)).storeImage(eq(expectedId), eq(expectedVideoResource));

    verify(videoGateway, times(1)).update(argThat(actualVideo ->
        actualVideo.getVideo().isEmpty()
            && actualVideo.getTrailer().isEmpty()
            && actualVideo.getBanner().isEmpty()
            && actualVideo.getThumbnail().isEmpty()
            && Objects.equals(expectedMedia, actualVideo.getThumbnailHalf().get())
    ));
  }

  @Test
  void givenCmdToUpload_whenVideoIsInvalid_shouldReturnNotFound() {
    // given
    final var aVideo = FixtureUtils.Videos.systemDesign();
    final var expectedId = aVideo.getId();
    final var expectedType = VideoMediaType.THUMBNAIL_HALF;
    final var expectedResource = FixtureUtils.Videos.resource(expectedType);
    final var expectedVideoResource = VideoResource.with(expectedType, expectedResource);

    final var expectedErrorMessage = "Video with ID %s was not found".formatted(expectedId.getValue());

    when(videoGateway.findById(any()))
        .thenReturn(Optional.empty());

    final var aCmd = UploadMediaCommand.with(expectedId.getValue(), expectedVideoResource);

    // when
    final var actualException = Assertions.assertThrows(
        NotFoundException.class,
        () -> useCase.execute(aCmd)
    );

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }
}