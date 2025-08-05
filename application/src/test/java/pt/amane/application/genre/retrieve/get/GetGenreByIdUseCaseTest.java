package pt.amane.application.genre.retrieve.get;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;

class GetGenreByIdUseCaseTest extends UseCaseTest {

  @InjectMocks
  private GetGenreByIdUseCaseImpl useCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway);
  }

  @Test
  void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
    // given
    final var espectedName = "Ação";
    final var espectedIsActivate = true;
    final var espectedIsCategories = List.of(
        CategoryID.from("123"),
        CategoryID.from("456"));

    // when
    final var aGenre = Genre.newGenre(espectedName, espectedIsActivate);

    aGenre.addCategories(espectedIsCategories);

    final var expectedId = aGenre.getId();

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.of(aGenre));

    final var actualGenre = useCase.execute(expectedId.getValue());

    // then
    assertEquals(espectedName, actualGenre.name());
    assertEquals(espectedIsActivate, actualGenre.isActive());
    assertEquals(espectedIsCategories.size(), actualGenre.categories().size());
    assertEquals(espectedIsCategories.get(0).getValue(), actualGenre.categories().get(0));
    assertEquals(espectedIsCategories.get(1).getValue(), actualGenre.categories().get(1));
  }

  @Test
  void givenAInvalidId_whenCallsGetGenre_shouldReturnNotFound() {
    // given
    final var expectedErrorMessage = "Genre with ID 123 was not found";
    final var expectedId = GenreID.from("123");

    Mockito.when(genreGateway.findById(Mockito.any()))
        .thenReturn(Optional.empty());

    //when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(expectedId.getValue());
    });

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    Mockito.verify(genreGateway, Mockito.times(1)).findById(expectedId);

  }

}