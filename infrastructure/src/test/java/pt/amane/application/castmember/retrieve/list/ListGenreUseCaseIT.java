package pt.amane.application.castmember.retrieve.list;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import pt.amane.IntegrationTest;
import pt.amane.application.genre.retrieve.list.GenreListOutput;
import pt.amane.application.genre.retrieve.list.ListGenreUseCaseImpl;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.infrastructure.genre.persistence.GenreJpaEntity;
import pt.amane.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
class ListGenreUseCaseIT {

  @Autowired
  private ListGenreUseCaseImpl useCase;

  @Autowired
  private GenreGateway genreGateway;

 @Autowired
 private GenreRepository genreRepository;

  @Test
  void givenAValidQuery_whenCallsListGenre_shouldReturnGenres() {
    // given
    final var genres = List.of(
        Genre.newGenre("Ação", true),
        Genre.newGenre("Aventura", true)
    );

    genreRepository.saveAllAndFlush(
        genres.stream().map(GenreJpaEntity::from).toList());

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = genres.stream()
        .map(GenreListOutput::from)
        .toList();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertTrue(
        expectedItems.size() == actualOutput.items().size()
      && expectedItems.containsAll(actualOutput.items()));
  }

  @Test
  void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_shouldReturnGenres() {
    // given
    final var genres = List.<Genre>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var expectedItems = List.<GenreListOutput>of();

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());
  }

}
