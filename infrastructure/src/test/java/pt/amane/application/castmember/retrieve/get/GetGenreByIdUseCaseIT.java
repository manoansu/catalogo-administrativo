package pt.amane.application.castmember.retrieve.get;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pt.amane.IntegrationTest;
import pt.amane.application.genre.retrieve.get.GetGenreByIdUseCase;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;

@IntegrationTest
class GetGenreByIdUseCaseIT {

  @Autowired
  private GetGenreByIdUseCase useCase;

  @Autowired
  private CategoryGateway categoryGateway;

  @Autowired
  private GenreGateway genreGateway;

  @Test
  void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
    // given
    final var series = categoryGateway.create(Category.newCategory("Séries", null, true));
    final var filmes = categoryGateway.create(Category.newCategory("Filmes", null, true));
    final var expectedName = "Ação";
    final var expectedIsActivate = true;
    final var expectedIsCategories = List.of(
        series.getId(),
        filmes.getId());

    // when
    final var aGenre = genreGateway.create(Genre.newGenre(expectedName, expectedIsActivate)
        .addCategories(expectedIsCategories));

    final var expectedId = aGenre.getId();

    final var actualGenre = useCase.execute(expectedId.getValue());

    // then
    assertEquals(expectedId.getValue(), actualGenre.id());
    assertEquals(expectedName, actualGenre.name());
    assertEquals(expectedIsActivate, actualGenre.isActive());
    assertTrue(expectedIsCategories.size() == actualGenre.categories().size());
    assertEquals(expectedIsCategories.size(), actualGenre.categories().size());
  }

  @Test
  void givenAInvalidId_whenCallsGetGenre_shouldReturnNotFound() {
    // given
    final var expectedErrorMessage = "Genre with ID 123 was not found";
    final var expectedId = GenreID.from("123");

    //when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(expectedId.getValue());
    });

    // then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  }

}
