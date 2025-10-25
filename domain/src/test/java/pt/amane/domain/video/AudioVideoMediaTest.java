package pt.amane.domain.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class
AudioVideoMediaTest {

  @Test
  void givenValidParams_whenCallsNewAudioVideo_ShouldReturnInstance() {

    //give
    final var expectedChecksum = "abc";
    final var expectedName = "video.mp4";
    final var expectedRawLocation = "/123/videos";
    final var expectedEncodedLocation = "";
    final var expectedMediaStatus = MediaStatus.PENDING;

    //when
    final var actualAudioVideo = AudioVideoMedia.with(expectedChecksum, expectedName,
        expectedRawLocation);

    //then
    assertNotNull(actualAudioVideo);
    assertNotNull(actualAudioVideo.id());
    assertEquals(expectedChecksum, actualAudioVideo.checksum());
    assertEquals(expectedName, actualAudioVideo.name());
    assertEquals(expectedRawLocation, actualAudioVideo.rawLocation());
    assertEquals(expectedEncodedLocation, actualAudioVideo.encodedLocation());
    assertEquals(expectedMediaStatus, actualAudioVideo.status());
  }

  @Test
  void givenTwoAudioVideoMediaWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {

    //give
    final var expectedChecksum = "abc";
    final var expectedName = "video.mp4";
    final var expectedRawLocation = "/123/videos";

    //when
    final var video1 = AudioVideoMedia.with(expectedChecksum, expectedName, expectedRawLocation);
    final var video2 = AudioVideoMedia.with(expectedChecksum, "any", expectedRawLocation);

    //then
    assertEquals(video1, video2);
    assertNotSame(video1, video2);
  }

  @Test
  void givenTwoAudioVideoMediaWithDifferentChecksumAndLocation_whenCallsEquals_shouldReturnFalse() {
    final var video1 = AudioVideoMedia.with("abc", "video1.mp4", "/123/videos");
    final var video2 = AudioVideoMedia.with("def", "video2.mp4", "/456/videos");

    assertNotEquals(video1, video2);
  }

  @Test
  void givenValidAudioVideoMedia_whenCallsProcessing_shouldReturnUpdated() {
    //given
    Assertions.assertThrows(
        NullPointerException.class,
        () -> AudioVideoMedia.with(null, "131", "Random", "/videos", "/videos", MediaStatus.PENDING)
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> AudioVideoMedia.with("id", "abc", null, "/videos", "/videos", MediaStatus.PENDING)
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> AudioVideoMedia.with("id", "abc", "Random", null, "/videos", MediaStatus.PENDING)
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> AudioVideoMedia.with("id", "abc", "Random", "/videos", null, MediaStatus.PENDING)
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> AudioVideoMedia.with("id", "abc", "Random", "/videos", "/videos", null)
    );
  }

}