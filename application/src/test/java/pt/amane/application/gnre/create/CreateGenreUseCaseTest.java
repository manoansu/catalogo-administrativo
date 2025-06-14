package pt.amane.application.gnre.create;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.genre.GenreGateway;

class CreateGenreUseCaseTest extends UseCaseTest {

  @InjectMocks
  private CreateGenreUseCaseImpl useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway, genreGateway);
  }

  @Test
  void givenAValidCommand_whenCallsCreateGenre_shouldReturnGenreId() {

    //given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    Mockito.when(genreGateway.create(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    //when
    final var actualOutput = useCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
        Objects.equals(expectedName, aGenre.getName())
            && Objects.equals(expectedIsActive, aGenre.isActive())
            && Objects.equals(expectedCategories, aGenre.getCategories())
            && Objects.nonNull(aGenre.getId())
            && Objects.nonNull(aGenre.getCreatedAt())
            && Objects.nonNull(aGenre.getUpdatedAt())
            && Objects.isNull(aGenre.getDeletedAt())
    ));

  }

  @Test
  void givenAValidCommandWithCategories_whenCallsCreateGenre_shouldReturnGenreId() {
    //given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(
        CategoryID.from("123"),
        CategoryID.from("456"));

    final var aCommand = CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

    Mockito.when(genreGateway.create(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    Mockito.when(categoryGateway.existsByIds(expectedCategories))
        .thenReturn(expectedCategories);

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(categoryGateway, times(1)).existsByIds(expectedCategories);

    Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
        Objects.equals(expectedName, aGenre.getName())
            && Objects.equals(expectedIsActive, aGenre.isActive())
            && Objects.equals(expectedCategories, aGenre.getCategories())
            && Objects.nonNull(aGenre.getId())
            && Objects.nonNull(aGenre.getCreatedAt())
            && Objects.nonNull(aGenre.getUpdatedAt())
            && Objects.isNull(aGenre.getDeletedAt())
    ));
  }

  @Test
  void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_shouldReturnGenreId() {
    // given
    final var expectName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aCommand =
        CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    when(genreGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(genreGateway, times(1)).create(argThat(aGenre ->
        Objects.equals(expectName, aGenre.getName())
            && Objects.equals(expectedIsActive, aGenre.isActive())
            && Objects.equals(expectedCategories, aGenre.getCategories())
            && Objects.nonNull(aGenre.getId())
            && Objects.nonNull(aGenre.getCreatedAt())
            && Objects.nonNull(aGenre.getUpdatedAt())
            && Objects.nonNull(aGenre.getDeletedAt())
    ));
  }

  @Test
  void givenAInvalidEmptyName_whenCallsCreateGenre_shouldReturnDomainException() {
    // given
    final var expectName = " ";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorMessage = "'name' should not be empty";
    final var expectedErrorCount = 1;

    final var aCommand =
        CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    Mockito.verify(categoryGateway, times(0)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

  @Test
  void givenAInvalidNullName_whenCallsCreateGenre_shouldReturnDomainException() {
    // given
    final String expectName = null;
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    final var aCommand =
        CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    Mockito.verify(categoryGateway, times(0)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

  @Test
  void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
    // given
    final var filmes = CategoryID.from("456");
    final var series = CategoryID.from("123");
    final var documentarios = CategoryID.from("789");

    final var expectName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series, documentarios);

    final var expectedErrorMessage = "Some categories could not be found: 456, 789";
    final var expectedErrorCount = 1;

    when(categoryGateway.existsByIds(any()))
        .thenReturn(List.of(series));

    final var aCommand =
        CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    Mockito.verify(categoryGateway, times(1)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

  @Test
  void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_shouldReturnDomainException() {
    // given
    final var filmes = CategoryID.from("456");
    final var series = CategoryID.from("123");
    final var documentarios = CategoryID.from("789");

    final var expectName = " ";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes, series, documentarios);

    final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
    final var expectedErrorMessageTwo = "'name' should not be empty";
    final var expectedErrorCount = 2;

    when(categoryGateway.existsByIds(any()))
        .thenReturn(List.of(series));

    final var aCommand =
        CreateGenreCommand.with(expectName, expectedIsActive, asString(expectedCategories));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
    Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());

    Mockito.verify(categoryGateway, times(1)).existsByIds(any());
    Mockito.verify(genreGateway, times(0)).create(any());
  }

}