package pt.amane.domain.category;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

// Assuming your CategoryID class is defined elsewhere and looks something like this:
/*
public final class CategoryID {
    private final String value;

    // Constructor is assumed to perform validation
    private CategoryID(final String anId) {
        if (anId == null) {
            throw new IllegalArgumentException("ID cannot be null");
        }
        if (anId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be empty or blank");
        }
        this.value = anId;
    }

    public static CategoryID from(final String anId) {
        return new CategoryID(anId);
    }

    public static CategoryID from(final UUID anId) {
        // Original code: return new CategoryID(anId.toString().toLowerCase());
        // If anId is null, anId.toString() will cause NullPointerException.
        return new CategoryID(anId.toString().toLowerCase());
    }

    public String getValue() {
        return this.value;
    }

    // equals and hashCode would be beneficial for a value object
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryID that = (CategoryID) o;
        return java.util.Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(value);
    }
}
*/

class CategoryIDTest {

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
      final UUID uuid = UUID.randomUUID();
      final String expectedId = uuid.toString().toLowerCase();

      // Act
      final CategoryID categoryID = CategoryID.from(uuid);

      // Assert
      assertNotNull(categoryID);
      assertEquals(expectedId, categoryID.getValue());
    }

    @Test
    @DisplayName("should ensure value is lowercase even if UUID string representation had uppercase (though UUID.toString() is typically lowercase)")
    void givenUUIDRepresentedWithUppercase_whenFromUUID_thenEnsuresLowercaseValue() {
      // Arrange
      // UUID.fromString accepts uppercase, UUID.toString() produces lowercase.
      // This test verifies the .toLowerCase() call in your from(UUID) method.
      final String uppercaseUuidString = "A987FBCB-4621-4A13-9057-79E085C7A5E0";
      final UUID uuid = UUID.fromString(uppercaseUuidString);
      final String expectedLowercaseId = uppercaseUuidString.toLowerCase();

      // Act
      final CategoryID categoryID = CategoryID.from(uuid);

      // Assert
      assertNotNull(categoryID);
      assertEquals(expectedLowercaseId, categoryID.getValue());
    }

    @Test
    @DisplayName("should throw NullPointerException when given a null UUID")
    void givenNullUUID_whenFromUUID_thenThrowsNullPointerException() {
      // Arrange
      final UUID nullUuid = null;

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
      final UUID uuid = UUID.randomUUID();
      final String expectedValue = uuid.toString().toLowerCase();
      final CategoryID categoryID = CategoryID.from(uuid);

      // Act
      final String actualValue = categoryID.getValue();

      // Assert
      assertEquals(expectedValue, actualValue);
    }
  }
}