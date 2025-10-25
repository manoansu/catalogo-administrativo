package pt.amane.domain.video;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class ImageMediaTest {


  @Test
  void givenValidParams_whenCallsNewImage_ShouldReturnInstance() {

    //give
    final var expectedChecksum = "abc";
    final var expectedName = "banner.png";
    final var expectedLocation = "/images/ac";

    //when
    final var actualImage = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);

    //then
    assertNotNull(actualImage);
    assertNotNull(actualImage.id());
    assertEquals(expectedChecksum, actualImage.checksum());
    assertEquals(expectedName, actualImage.name());
    assertEquals(expectedLocation, actualImage.location());
  }

  @Test
  void givenTwoImagesWithSameChecksumAndLocation_whenCallsEquals_ShouldReturnTrue() {

    //give
    final var expectedChecksum = "abc";
    final var expectedName = "banner.png";
    final var expectedLocation = "/images/acb";

    //when
    final var image1 = ImageMedia.with(expectedChecksum, expectedName, expectedLocation);
    final var image2 = ImageMedia.with(expectedChecksum, "any", expectedLocation);

    //then
    assertEquals(image1, image2);
    assertNotSame(image1, image2);
  }

  @Test
  void givenTwoImagesWithDifferentChecksumAndLocation_whenCallsEquals_shouldReturnFalse() {
    final var image1 = ImageMedia.with("abc", "banner.png", "/images/acb");
    final var image2 = ImageMedia.with("def", "banner.png", "/images/def");

    assertNotEquals(image1, image2);
  }

  @Test
  void givenInvalidParams_whenCallsWith_ShouldReturnError() {

    //given
    Assertions.assertThrows(
        NullPointerException.class,
        () -> ImageMedia.with(null, "Random", "/images")
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> ImageMedia.with("abc", null, "/images")
    );

    Assertions.assertThrows(
        NullPointerException.class,
        () -> ImageMedia.with("abc", "Random", null)
    );

  }


}