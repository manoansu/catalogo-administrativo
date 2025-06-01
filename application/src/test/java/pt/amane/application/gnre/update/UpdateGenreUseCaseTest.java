package pt.amane.application.gnre.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;

class UpdateGenreUseCaseTest extends UseCaseTest {

  @InjectMocks
  private UpdateGenreUseCaseImpl useCase;

  @Mock
  private GenreGateway genreGateway;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway, categoryGateway);
  }

  @Test
  void givenAValidCommand_whenCallsUpdateGenre_shouldReturnGenreId() {
    // given
    final var aGenre = Genre.newGenre("acao", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findById(any()))
        .thenReturn(Optional.of(Genre.with(aGenre)));

    when(genreGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    final var updatedGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(genreGateway, times(1)).update(updatedGenre);

    Assertions.assertNotNull(updatedGenre);
    Assertions.assertEquals(expectedId, updatedGenre.getId());
    Assertions.assertEquals(expectedName, updatedGenre.getName());
    Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
    Assertions.assertEquals(expectedCategories, updatedGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), updatedGenre.getUpdatedAt());
    Assertions.assertNull(updatedGenre.getDeletedAt());

  }

  @Test
  void givenAValidCommandWithCategories_whenCallsUpdateGenre_shouldReturnGenreId() {
    // given
    final var aGenre = Genre.newGenre("acao", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("123"),
        CategoryID.from("456")
    );

    final var aCommand = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findById(any()))
        .thenReturn(Optional.of(Genre.with(aGenre)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(expectedCategories);

    when(genreGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    final var updatedGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

    Mockito.verify(genreGateway, times(1)).update(updatedGenre);

    Assertions.assertNotNull(updatedGenre);
    Assertions.assertEquals(expectedId, updatedGenre.getId());
    Assertions.assertEquals(expectedName, updatedGenre.getName());
    Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
    Assertions.assertEquals(expectedCategories, updatedGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), updatedGenre.getUpdatedAt());
    Assertions.assertNull(updatedGenre.getDeletedAt());
  }

  @Test
  void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_shouldReturnGenreId() {
    // given
    final var aGenre = Genre.newGenre("acao", true);

    final var expectedId = aGenre.getId();
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findById(any()))
        .thenReturn(Optional.of(Genre.with(aGenre)));

    when(genreGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    Assertions.assertTrue(aGenre.isActive());
    Assertions.assertNull(aGenre.getDeletedAt());

    // when
    final var actualOutput = useCase.execute(aCommand);

    final var updatedGenre = aGenre.update(expectedName, expectedIsActive, expectedCategories);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(genreGateway, times(1)).update(updatedGenre);

    Assertions.assertNotNull(updatedGenre);
    Assertions.assertEquals(expectedId, updatedGenre.getId());
    Assertions.assertEquals(expectedName, updatedGenre.getName());
    Assertions.assertEquals(expectedIsActive, updatedGenre.isActive());
    Assertions.assertEquals(expectedCategories, updatedGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), updatedGenre.getCreatedAt());
    Assertions.assertEquals(aGenre.getUpdatedAt(), updatedGenre.getUpdatedAt());
    Assertions.assertNotNull(updatedGenre.getDeletedAt());
  }

  @Test
  void givenAnInvalidName_whenCallsUpdateGenre_shouldReturnNotificationException() {
    // given
    final var aGenre = Genre.newGenre("acao", true);

    final var expectedId = aGenre.getId();
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findById(any()))
        .thenReturn(Optional.of(Genre.with(aGenre)));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(categoryGateway, times(0)).existsByIds(any());

    Mockito.verify(genreGateway, times(0)).update(any());
  }

  @Test
  void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_shouldReturnNotificationException() {
    // given
    final var filmes = CategoryID.from("123");
    final var series = CategoryID.from("456");
    final var documentarios = CategoryID.from("789");

    final var aGenre = Genre.newGenre("acao", true);

    final var expectedId = aGenre.getId();
    final String expectedName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series, documentarios);

    final var expectedErrorCount = 2;
    final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
    final var expectedErrorMessageTwo = "'name' should not be null";

    final var aCommand = UpdateGenreCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedIsActive,
        asString(expectedCategories)
    );

    when(genreGateway.findById(any()))
        .thenReturn(Optional.of(Genre.with(aGenre)));

    when(categoryGateway.existsByIds(any()))
        .thenReturn(List.of(filmes));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

    Mockito.verify(genreGateway, times(1)).findById(eq(expectedId));

    Mockito.verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));

    Mockito.verify(genreGateway, times(0)).update(any());
  }

}