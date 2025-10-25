package pt.amane.infrastructure.genre;

import java.util.Comparator;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pt.amane.Main;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.infrastructure.genre.persistence.GenreJpaEntity;
import pt.amane.infrastructure.genre.persistence.GenreRepository;

@ActiveProfiles("test-integration")
@SpringBootTest(classes = Main.class)
@Transactional
class GenreGatewayImplTest {

  @Autowired
  private CategoryGateway categoryGateway;

  @Autowired
  private GenreGateway genreGateway;

  @Autowired
  private GenreRepository genreRepository;

  @Test
  void testDependenciesInjected() {
    Assertions.assertNotNull(categoryGateway);
    Assertions.assertNotNull(genreGateway);
    Assertions.assertNotNull(genreRepository);
  }

  @Test
  void givenAValidGenre_whenCallsCreateGenre_shouldPersistGenre() {
    // given
    final var filmes =
        categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    aGenre.addCategories(expectedCategories);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.create(aGenre);

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertEquals(actualGenre.getCreatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertNotNull(persistedGenre.getCreatedAt());
    Assertions.assertNotNull(persistedGenre.getUpdatedAt());
    Assertions.assertEquals(persistedGenre.getCreatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenreWithoutCategories_whenCallsCreateGenre_shouldPersistGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.create(aGenre);

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertEquals(actualGenre.getCreatedAt(), actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertNotNull(persistedGenre.getCreatedAt());
    Assertions.assertNotNull(persistedGenre.getUpdatedAt());
    Assertions.assertEquals(persistedGenre.getCreatedAt(), persistedGenre.getUpdatedAt());
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_shouldPersistGenre() {
    // given
    final var filmes =
        categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var series =
        categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId(), series.getId());

    final var aGenre = Genre.newGenre("ac", expectedIsActive);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals("ac", aGenre.getName());
    Assertions.assertEquals(0, aGenre.getCategories().size());

    // when
    final var actualGenre = genreGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertIterableEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertIterableEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoryIDs()));
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_shouldPersistGenre() {
    // given
    final var filmes =
        categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var series =
        categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre("ac", expectedIsActive);
    aGenre.addCategories(List.of(filmes.getId(), series.getId()));

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals("ac", aGenre.getName());
    Assertions.assertEquals(2, aGenre.getCategories().size());

    // when
    final var actualGenre = genreGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenreInactive_whenCallsUpdateGenreActivating_shouldPersistGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, false);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertFalse(aGenre.isActive());
    Assertions.assertNotNull(aGenre.getDeletedAt());

    // when
    final var actualGenre = genreGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenAValidGenreActive_whenCallsUpdateGenreInactivating_shouldPersistGenre() {
    // given
    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.<CategoryID>of();

    final var aGenre = Genre.newGenre(expectedName, true);

    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertTrue(aGenre.isActive());
    Assertions.assertNull(aGenre.getDeletedAt());

    // when
    final var actualGenre = genreGateway.update(
        Genre.with(aGenre)
            .update(expectedName, expectedIsActive, expectedCategories)
    );

    // then
    Assertions.assertEquals(1, genreRepository.count());

    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
    Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
    Assertions.assertNotNull(actualGenre.getDeletedAt());

    final var persistedGenre = genreRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, persistedGenre.getName());
    Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
    Assertions.assertEquals(expectedCategories, persistedGenre.getCategoryIDs());
    Assertions.assertEquals(aGenre.getCreatedAt(), persistedGenre.getCreatedAt());
    Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persistedGenre.getUpdatedAt()));
    Assertions.assertNotNull(persistedGenre.getDeletedAt());
  }

  @Test
  void givenTwoGenresAndOnePersisted_whenCallsExistsByIds_shouldReturnPersistedID() {
    // given
    final var aGenre = Genre.newGenre("Genre 1", true);

    final var expectedItems = 1;
    final var expectedId = aGenre.getId();

    Assertions.assertEquals(0, genreRepository.count());

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    // when
    final var actualGenre = genreGateway.existsByIds(List.of(GenreID.from("123"), expectedId));

    // then
    Assertions.assertEquals(expectedItems, actualGenre.size());
    Assertions.assertEquals(expectedId.getValue(), actualGenre.get(0).getValue());
  }

  @Test
  void givenAPrePersistedGenre_whenCallsDeleteById_shouldDeleteGenre() {
    // given
    final var aGenre = Genre.newGenre("Ação", true);

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals(1, genreRepository.count());

    // when
    genreGateway.deleteById(aGenre.getId());

    // then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  void givenAnInvalidGenre_whenCallsDeleteById_shouldReturnOK() {
    // given
    Assertions.assertEquals(0, genreRepository.count());

    // when
    genreGateway.deleteById(GenreID.from("123"));

    // then
    Assertions.assertEquals(0, genreRepository.count());
  }

  @Test
  void givenAPrePersistedGenre_whenCallsFindById_shouldReturnGenre() {
    // given
    final var filmes =
        categoryGateway.create(Category.newCategory("Filmes", null, true));

    final var series =
        categoryGateway.create(Category.newCategory("Séries", null, true));

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes.getId(), series.getId());

    final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
    aGenre.addCategories(expectedCategories);

    final var expectedId = aGenre.getId();

    genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

    Assertions.assertEquals(1, genreRepository.count());

    // when
    final var actualGenre = genreGateway.findById(expectedId).get();

    // then
    Assertions.assertEquals(expectedId, actualGenre.getId());
    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void givenAInvalidGenreId_whenCallsFindById_shouldReturnEmpty() {
    // given
    final var expectedId = GenreID.from("123");

    Assertions.assertEquals(0, genreRepository.count());

    // when
    final var actualGenre = genreGateway.findById(expectedId);

    // then
    Assertions.assertTrue(actualGenre.isEmpty());
  }

  @Test
  void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());
  }

  @ParameterizedTest
  @CsvSource({
      "aç,0,10,1,1,Ação",
      "dr,0,10,1,1,Drama",
      "com,0,10,1,1,Comédia romântica",
      "cien,0,10,1,1,Ficção científica",
      "terr,0,10,1,1,Terror",
  })
  void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ) {
    // given
    mockGenres();

    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
  }

  @ParameterizedTest
  @CsvSource({
      "name,asc,0,10,5,5,Ação",
      "name,desc,0,10,5,5,Terror",
      "createdAt,asc,0,10,5,5,Comédia romântica",
      "createdAt,desc,0,10,5,5,Ficção científica",
  })
  void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenreName
  ) {
    // given
    mockGenres();

    final var expectedTerms = "";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
  }

  @ParameterizedTest
  @CsvSource({
      "0,2,2,5,Ação;Comédia romântica",
      "1,2,2,5,Drama;Ficção científica",
      "2,2,1,5,Terror",
  })
  void givenAValidPaging_whenCallsFindAll_shouldReturnPaginated(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedGenres
  ) {
    // given
    mockGenres();

    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = genreGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

    int index = 0;
    for (final var expectedName : expectedGenres.split(";")) {
      Assertions.assertEquals(expectedName, actualPage.items().get(index).getName());
      index++;
    }
  }

  private void mockGenres() {
    try {
        categoryGateway.create(Category.newCategory("Comédia romântica", null, true));
        Thread.sleep(1);
        categoryGateway.create(Category.newCategory("Ação", null, true));
        Thread.sleep(1);
        categoryGateway.create(Category.newCategory("Drama", null, true));
        Thread.sleep(1);
        categoryGateway.create(Category.newCategory("Terror", null, true));
        Thread.sleep(1);
        categoryGateway.create(Category.newCategory("Ficção científica", null, true));
        Thread.sleep(1);

        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)));
        Thread.sleep(1);
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Ação", true)));
        Thread.sleep(1);
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Drama", true)));
        Thread.sleep(1);
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Terror", true)));
        Thread.sleep(1);
        genreRepository.saveAndFlush(GenreJpaEntity.from(Genre.newGenre("Ficção científica", true)));
    } catch (InterruptedException e) {
        // ignore
    }
  }

  private List<CategoryID> sorted(final List<CategoryID> expectedCategories) {
    return expectedCategories.stream()
        .sorted(Comparator.comparing(CategoryID::getValue))
        .toList();
  }
}
