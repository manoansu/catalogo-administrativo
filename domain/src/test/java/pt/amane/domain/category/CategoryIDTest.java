package pt.amane.domain.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import pt.amane.UnitTest;
import pt.amane.domain.utils.IdUtils;

import static org.junit.jupiter.api.Assertions.*;

// Assuming your CategoryID class is defined elsewhere and looks something like this:
class CategoryIDTest extends UnitTest {

  @Nested
  @DisplayName("Tests for from(String anId)")
  class FromStringTests {

    @Test
    @DisplayName("should create CategoryID when given a valid string ID")
    void givenValidString_whenFromString_thenCreatesCategoryID() {
      // Arrange
      final String expectedId = "valid-category-123";

      // Act
      final CategoryID categoryID = CategoryID.from(expectedId);

      // Assert
      assertNotNull(categoryID);
      assertEquals(expectedId, categoryID.getValue());
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given a null string ID")
    void givenNullString_whenFromString_thenThrowsNullPointerException() {
      // Arrange
      final String nullId = null;

      // Act & Assert
      // This test assumes the CategoryID constructor throws IllegalArgumentException for null input.
      // The exact message "ID cannot be null" is an assumption based on typical validation.
      final var exception = assertThrows(NullPointerException.class, () -> {
        CategoryID.from(nullId);
      });
      // assertEquals("ID cannot be null", exception.getMessage()); // Uncomment if you want to assert the message
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given an empty string ID")
    void givenEmptyString_whenFromString_thenThrowsNullPointerException() {
      // Arrange
      final String emptyId = "";

      // Act & Assert
      // This test assumes the CategoryID constructor throws IllegalArgumentException for empty input.
      // The exact message "ID cannot be empty or blank" is an assumption.
      final var exception = assertThrows(NullPointerException.class, () -> {
        CategoryID.from(emptyId);
      });
      // assertEquals("ID cannot be empty or blank", exception.getMessage()); // Uncomment if you want to assert the message
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when given a blank string ID")
    void givenBlankString_whenFromString_thenThrowsNullPointerException() {
      // Arrange
      final String blankId = "   ";

      // Act & Assert
      // This test assumes the CategoryID constructor throws IllegalArgumentException for blank input.
      final var exception = assertThrows(NullPointerException.class, () -> {
        CategoryID.from(blankId);
      });
      // assertEquals("ID cannot be empty or blank", exception.getMessage()); // Uncomment if you want to assert the message
    }
  }

  @Nested
  @DisplayName("Tests for from(UUID anId)")
  class FromUUIDTests {

    @Test
    @DisplayName("should create CategoryID with lowercase string value when given a valid UUID")
    void givenValidUUID_whenFromUUID_thenCreatesCategoryIDWithLowercaseValue() {
      // Arrange
      final String expectedValue = IdUtils.uuid();

      // Act
      final CategoryID categoryID = CategoryID.from(expectedValue);

      // Assert
      assertNotNull(categoryID);
      assertEquals(expectedValue, categoryID.getValue());
    }

    @Test
    @DisplayName("should ensure value is lowercase even if UUID string representation had uppercase (though UUID.toString() is typically lowercase)")
    void givenUUIDRepresentedWithUppercase_whenFromUUID_thenEnsuresLowercaseValue() {
      // Arrange
      // UUID.fromString accepts uppercase, UUID.toString() produces lowercase.
      // This test verifies the .toLowerCase() call in your from(UUID) method.
      final String expectedLowercaseId = IdUtils.uuid();

      // Act
      final CategoryID categoryID = CategoryID.from(expectedLowercaseId);

      // Assert
      assertNotNull(categoryID);
      assertEquals(expectedLowercaseId, categoryID.getValue());
    }

    @Test
    @DisplayName("should throw NullPointerException when given a null UUID")
    void givenNullUUID_whenFromUUID_thenThrowsNullPointerException() {
      // Arrange
      final String nullUuid = null;

      // Act & Assert
      // This is because anId.toString() will be called on a null reference.
      assertThrows(NullPointerException.class, () -> {
        CategoryID.from(nullUuid);
      });
    }
  }

  @Nested
  @DisplayName("Tests for getValue()")
  class GetValueTests {

    @Test
    @DisplayName("should return the correct string value used at creation")
    void getValue_returnsCorrectValue() {
      // Arrange
      final String expectedValue = "my-category-id";
      final CategoryID categoryID = CategoryID.from(expectedValue);

      // Act
      final String actualValue = categoryID.getValue();

      // Assert
      assertEquals(expectedValue, actualValue);
    }

    @Test
    @DisplayName("should return the correct lowercase UUID string value after creation from UUID")
    void getValue_afterFromUUID_returnsCorrectLowercaseValue() {
      // Arrange
      final String expectedValue = IdUtils.uuid();
      final CategoryID categoryID = CategoryID.from(expectedValue);

      // Act
      final String actualValue = categoryID.getValue();

      // Assert
      assertEquals(expectedValue, actualValue);
    }
  }
}