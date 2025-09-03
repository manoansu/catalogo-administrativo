package pt.amane.application.video.media.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.MediaStatus;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoMediaType;

class UpdateMediaStatusUseCaseTest extends UseCaseTest {

  @InjectMocks
  private UpdateMediaStatusUseCaseImpl useCase;

  @Mock
  private VideoGateway videoGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway);
  }

  @Test
  void givenCommandForVideo_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedFolder = "encoded_media";
    final var expectedFilename = "filename.mp4";
    final var expectedType = VideoMediaType.VIDEO;
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    final var aVideo = FixtureUtils.Videos.systemDesign()
        .updateVideoMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UpdateMediaStatusCommand.with(
        expectedStatus,
        expectedId.getValue(),
        expectedMedia.id(),
        expectedFolder,
        expectedFilename
    );

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

    final var actualVideoMedia = actualVideo.getVideo().get();

    Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
    Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
    Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
  }

  @Test
  void givenCommandForVideo_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.PROCESSING;
    final String expectedFolder = null;
    final String expectedFilename = null;
    final var expectedType = VideoMediaType.VIDEO;
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    final var aVideo = FixtureUtils.Videos.systemDesign()
        .updateVideoMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UpdateMediaStatusCommand.with(
        expectedStatus,
        expectedId.getValue(),
        expectedMedia.id(),
        expectedFolder,
        expectedFilename
    );

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());

    final var actualVideoMedia = actualVideo.getVideo().get();

    Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
    Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
    Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
  }

  @Test
  void givenCommandForTrailer_whenIsValid_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedFolder = "encoded_media";
    final var expectedFilename = "filename.mp4";
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    final var aVideo = FixtureUtils.Videos.systemDesign()
        .updateTrailerMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UpdateMediaStatusCommand.with(
        expectedStatus,
        expectedId.getValue(),
        expectedMedia.id(),
        expectedFolder,
        expectedFilename
    );

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    Assertions.assertTrue(actualVideo.getVideo().isEmpty());

    final var actualVideoMedia = actualVideo.getTrailer().get();

    Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
    Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
    Assertions.assertEquals(expectedFolder.concat("/").concat(expectedFilename), actualVideoMedia.encodedLocation());
  }

  @Test
  void givenCommandForTrailer_whenIsValidForProcessing_shouldUpdateStatusAndEncodedLocation() {
    // given
    final var expectedStatus = MediaStatus.PROCESSING;
    final String expectedFolder = null;
    final String expectedFilename = null;
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    final var aVideo = FixtureUtils.Videos.systemDesign()
        .updateTrailerMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    when(videoGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    final var aCmd = UpdateMediaStatusCommand.with(
        expectedStatus,
        expectedId.getValue(),
        expectedMedia.id(),
        expectedFolder,
        expectedFilename
    );

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(1)).findById(eq(expectedId));

    final var captor = ArgumentCaptor.forClass(Video.class);

    verify(videoGateway, times(1)).update(captor.capture());

    final var actualVideo = captor.getValue();

    Assertions.assertTrue(actualVideo.getVideo().isEmpty());

    final var actualVideoMedia = actualVideo.getTrailer().get();

    Assertions.assertEquals(expectedMedia.id(), actualVideoMedia.id());
    Assertions.assertEquals(expectedMedia.rawLocation(), actualVideoMedia.rawLocation());
    Assertions.assertEquals(expectedMedia.checksum(), actualVideoMedia.checksum());
    Assertions.assertEquals(expectedStatus, actualVideoMedia.status());
    Assertions.assertTrue(actualVideoMedia.encodedLocation().isBlank());
  }

  @Test
  void givenCommandForTrailer_whenIsInvalid_shouldDoNothing() {
    // given
    final var expectedStatus = MediaStatus.COMPLETED;
    final var expectedFolder = "encoded_media";
    final var expectedFilename = "filename.mp4";
    final var expectedType = VideoMediaType.TRAILER;
    final var expectedMedia = FixtureUtils.Videos.audioVideo(expectedType);

    final var aVideo = FixtureUtils.Videos.systemDesign()
        .updateTrailerMedia(expectedMedia);

    final var expectedId = aVideo.getId();

    when(videoGateway.findById(any()))
        .thenReturn(Optional.of(aVideo));

    final var aCmd = UpdateMediaStatusCommand.with(
        expectedStatus,
        expectedId.getValue(),
        "randomId",
        expectedFolder,
        expectedFilename
    );

    // when
    this.useCase.execute(aCmd);

    // then
    verify(videoGateway, times(0)).update(any());
  }
}