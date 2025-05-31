package pt.amane.domain.genre;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.category.UnitTest;
import pt.amane.domain.exception.NotificationException;

class GenreTest extends UnitTest {

  @Test
  void givenAValidParams_whenCallsNewGenre_shouldInstantiateAGenre() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAnInvalidEmptyName_whenCallsNewGenreAndValidate_shouldReceiveError() {
    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";

     final var actualException = Assertions.assertThrows(NotificationException.class,
        () -> Genre.newGenre(expectedName, expectedIsActive));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  void givenInvalidNullName_whenCallNewGenreAndValidate_shouldReceiveAError() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var actualException = Assertions.assertThrows(NotificationException.class,
        () -> Genre.newGenre(expectedName, expectedIsActive));

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  void givenAnInvalidNameLengthGreaterThan255_whenCallsNewGenreAndValidate_shouldReceiveError() {
    final var expectedName = """
        Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
        composição das posturas dos órgãos dirigentes com relação às suas atribuições.
        Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
        manutenção das novas proposições.
        """;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      Genre.newGenre(expectedName, expectedIsActive);
    });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  void givenAnActiveGenre_whenCallDeactivate_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, true);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertTrue(actualGenre.isActive());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.update(expectedName, expectedIsActive, null);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());

    actualGenre.deactivate();

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getDeletedAt());

  }

  @Test
  void givenAnInactiveGenre_whenCallActivate_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = 0;

    final var actualGenre = Genre.newGenre(expectedName, false);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertFalse(actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.update(expectedName, expectedIsActive, null);

    actualGenre.activate();

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

  }

  @Test
  void givenAValidInactiveGenre_whenCallUpdateWithActivate_shouldReceiveGenreUpdated() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(CategoryID.from("123"));

    final var actualGenre = Genre.newGenre(expectedName, false);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertFalse(actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.update(expectedName, expectedIsActive, null);

    actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidActiveGenre_whenCallUpdateWithInactivate_shouldReceiveGenreUpdated() {
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.of(CategoryID.from("123"));

    final var actualGenre = Genre.newGenre("acao", true);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertTrue(actualGenre.isActive());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt =actualGenre.update(expectedName, expectedIsActive, null);

    actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenre_whenCallUpdateWithEmptyName_shouldReceiveNotificationException() {
    final var expectedName = " ";
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be empty";
    final var expectedCategories = List.of(CategoryID.from("123"));

    final var actualGenre = Genre.newGenre("Ação", true);

    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      actualGenre.update(expectedName, expectedIsActive, expectedCategories);
    });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
  }

  @Test
  void givenAValidGenre_whenCallUpdateWithNullName_shouldReceiveNotificationException() {
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedCategories = List.of(CategoryID.from("123"));

    final var actualGenre = Genre.newGenre("Ação", true);

    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      actualGenre.update(expectedName, expectedIsActive, expectedCategories);
    });

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

  @Test
  void givenAValidGenre_whenCallUpdateWithNullCategories_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = new ArrayList<CategoryID>();

    final var actualGenre = Genre.newGenre("Ação", true);

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.update(expectedName, expectedIsActive, null);

    Assertions.assertDoesNotThrow(() -> {
      actualGenre.update(expectedName, expectedIsActive, null);
    });

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidEmptyCategoriesGenre_whenCallAddCategory_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var actualGenre = Genre.newGenre(expectedName, true);

    Assertions.assertNotNull(actualGenre);
    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertTrue(actualGenre.isActive());
    Assertions.assertNull(actualGenre.getDeletedAt());
    Assertions.assertEquals(0, actualGenre.getCategories().size());


    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.update(expectedName, expectedIsActive, null);

    actualGenre.addCategory(CategoryID.from("123"));
    actualGenre.addCategory(CategoryID.from("456"));

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(2, actualGenre.getCategories().size());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt.getUpdatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAInvalidNullAsCategoryID_whenCallAddCategory_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = new ArrayList<CategoryID>();

    final var actualGenre = Genre.newGenre(expectedName, true);

    Assertions.assertEquals(0, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.addCategory(null);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    final var seriesID = CategoryID.from("123");
    final var moviesID = CategoryID.from("456");

    final var expectedCategories = List.of(moviesID);

    actualGenre.update(expectedName, expectedIsActive, List.of(seriesID, moviesID));

    Assertions.assertEquals(2, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.removeCategory(seriesID);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAnInvalidNullAsCategoryID_whenCallRemoveCategory_shouldReceiveOK() {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    final var seriesID = CategoryID.from("123");
    final var moviesID = CategoryID.from("456");

    final var expectedCategories = List.of(seriesID, moviesID);

    actualGenre.update(expectedName, expectedIsActive, expectedCategories);

    Assertions.assertEquals(2, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.removeCategory(null);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidEmptyCategoriesGenre_whenCallAddCategories_shouldReceiveOK() {
    final var seriesID = CategoryID.from("123");
    final var moviesID = CategoryID.from("456");

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(seriesID, moviesID);

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertEquals(0, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.addCategories(expectedCategories);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithEmptyList_shouldReceiveOK() {

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertEquals(0, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.addCategories(expectedCategories);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAValidEmptyCategoriesGenre_whenCallAddCategoriesWithNullList_shouldReceiveOK() {

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

    Assertions.assertEquals(0, actualGenre.getCategories().size());

    final var actualCreatedAt = actualGenre.getCreatedAt();
    final var actualUpdatedAt = actualGenre.getUpdatedAt();

    actualGenre.addCategories(null);

    Assertions.assertNotNull(actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(actualCreatedAt, actualGenre.getCreatedAt());
    Assertions.assertEquals(actualUpdatedAt, actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

}