package pt.amane.application.genre.delete;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;

class DeleteGenreUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DeleteGenreUseCaseImpl useCase;

  @Mock
  private GenreGateway genreGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(genreGateway);
  }

  @Test
  void givenAValidGenreId_whenCallsDeleteGenre_shouldDeleteGenre() {
    // given
    final var aGenre = Genre.newGenre("Ação", true);

    final var expectedId = aGenre.getId();

    doNothing()
        .when(genreGateway).deleteById(any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // Then
    Mockito.verify(genreGateway, times(1)).deleteById(expectedId);

  }

  @Test
  void givenAnInvalidGenreId_whenCallsDeleteGenre_shouldBeOk() {
    // given
    final var expectedId = GenreID.from("123");

    doNothing()
        .when(genreGateway).deleteById(any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // Then
    Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
  }

  @Test
  void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_shouldReceiveException() {
    // given
    final var aGenre = Genre.newGenre("Ação", true);
    final var expectedId = aGenre.getId();

    doThrow(new IllegalStateException("Gateway error"))
        .when(genreGateway).deleteById(any());

    // when
    Assertions.assertThrows(IllegalStateException.class, () -> {
      useCase.execute(expectedId.getValue());
    });

    // Then
    Mockito.verify(genreGateway, times(1)).deleteById(expectedId);
  }

}