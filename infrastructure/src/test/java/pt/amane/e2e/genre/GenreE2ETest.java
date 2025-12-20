package pt.amane.e2e.genre;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.amane.E2ETest;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.GenreID;
import pt.amane.e2e.E2ETestListener;
import pt.amane.e2e.MockDsl;
import pt.amane.e2e.MySQLCleanUpExtension;
import pt.amane.infrastructure.genre.models.UpdateGenreRequest;
import pt.amane.infrastructure.genre.persistence.GenreRepository;

@E2ETest
@Testcontainers
@ExtendWith(MySQLCleanUpExtension.class)
public class GenreE2ETest extends E2ETestListener implements MockDsl {

  @Autowired
  private MockMvc mvc;

  @Autowired
  private GenreRepository genreRepository;

  @BeforeEach
  @Transactional
  void clean() {
    genreRepository.deleteAll();
  }

  @Override
  public MockMvc mvc() {
    return this.mvc;
  }

  @Test
  void asACatalogAdminIShouldBeAbleToCreateANewGenreWithValidValues() throws Exception {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

    final var actualGenre = genreRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategoryIDs().size());
    Assertions.assertTrue(expectedCategories.containsAll(actualGenre.getCategoryIDs()));
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToCreateANewGenreWithCategories() throws Exception {
    final var filmes = givenACategory("Filmes", null, true);

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes);

    final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

    final var actualGenre = genreRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategoryIDs().size());
    Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).containsAll(mapTo(actualGenre.getCategoryIDs(), CategoryID::getValue)));
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToNavigateThruAllGenres() throws Exception {
    givenAGenre("Ação", true, List.of());
    givenAGenre("Esportes", true, List.of());
    givenAGenre("Drama", true, List.of());

    listGenres(0, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Ação")));

    listGenres(1, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(1)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));

    listGenres(2, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(2)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")));

    listGenres(3, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(3)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(0)));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToSearchBetweenAllGenres() throws Exception {
    givenAGenre("Ação", true, List.of());
    givenAGenre("Esportes", true, List.of());
    givenAGenre("Drama", true, List.of());

    listGenres(0, 1, "dra")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(1)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Drama")));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToSortAllGenresByNameDesc() throws Exception {
    givenAGenre("Ação", true, List.of());
    givenAGenre("Esportes", true, List.of());
    givenAGenre("Drama", true, List.of());

    listGenres(0, 3, "", "name", "desc")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(3)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Esportes")))
        .andExpect(jsonPath("$.items[1].name", equalTo("Drama")))
        .andExpect(jsonPath("$.items[2].name", equalTo("Ação")));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToGetAGenreByItsIdentifier() throws Exception {
    final var filmes = givenACategory("Filmes", null, true);

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes);

    final var actualId = givenAGenre(expectedName, expectedIsActive, expectedCategories);

    final var actualGenre = retrieveAGenre(actualId);

    Assertions.assertEquals(expectedName, actualGenre.name());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.categories().size());
    Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).containsAll(actualGenre.categories()));
    Assertions.assertEquals(expectedIsActive, actualGenre.active());
    Assertions.assertNotNull(actualGenre.createdAt());
    Assertions.assertNotNull(actualGenre.updatedAt());
    Assertions.assertNull(actualGenre.deletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundGenre() throws Exception {
    final var aRequest = get("/genres/123")
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    this.mvc.perform(aRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", equalTo("Genre with ID 123 was not found")));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToUpdateAGenreByItsIdentifier() throws Exception {
    final var filmes = givenACategory("Filmes", null, true);

    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.of(filmes);

    final var actualId = givenAGenre("acao", true, List.of());

    final var aRequestBody = new UpdateGenreRequest(
        expectedName,
        mapTo(expectedCategories, CategoryID::getValue),
        expectedIsActive
    );

    updateAGenre(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualGenre = genreRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategoryIDs().size());
    Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).containsAll(mapTo(actualGenre.getCategoryIDs(), CategoryID::getValue)));
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToInactivateAGenreByItsIdentifier() throws Exception {
    final var filmes = givenACategory("Filmes", null, true);

    final var expectedName = "Ação";
    final var expectedIsActive = false;
    final var expectedCategories = List.of(filmes);

    final var actualId = givenAGenre(expectedName, true, expectedCategories);

    final var aRequestBody = new UpdateGenreRequest(
        expectedName,
        mapTo(expectedCategories, CategoryID::getValue),
        expectedIsActive
    );

    updateAGenre(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualGenre = genreRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategoryIDs().size());
    Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).containsAll(mapTo(actualGenre.getCategoryIDs(), CategoryID::getValue)));
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNotNull(actualGenre.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToActivateAGenreByItsIdentifier() throws Exception {
    final var expectedName = "Ação";
    final var expectedIsActive = true;
    final var expectedCategories = List.<CategoryID>of();

    final var actualId = givenAGenre(expectedName, false, expectedCategories);

    final var aRequestBody = new UpdateGenreRequest(
        expectedName,
        mapTo(expectedCategories, CategoryID::getValue),
        expectedIsActive
    );

    updateAGenre(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualGenre = genreRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualGenre.getName());
    Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategoryIDs().size());
    Assertions.assertTrue(mapTo(expectedCategories, CategoryID::getValue).containsAll(mapTo(actualGenre.getCategoryIDs(), CategoryID::getValue)));
    Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
    Assertions.assertNotNull(actualGenre.getCreatedAt());
    Assertions.assertNotNull(actualGenre.getUpdatedAt());
    Assertions.assertNull(actualGenre.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToDeleteAGenreByItsIdentifier() throws Exception {
    final var filmes = givenACategory("Filmes", null, true);

    final var actualId = givenAGenre("Ação", true, List.of(filmes));

    deleteAGenre(actualId)
        .andExpect(status().isNoContent());

    Assertions.assertFalse(this.genreRepository.existsById(actualId.getValue()));
  }

  @Test
  void asACatalogAdminIShouldNotSeeAnErrorByDeletingANotExistentGenre() throws Exception {
    deleteAGenre(GenreID.from("12313"))
        .andExpect(status().isNoContent());
    Assertions.assertEquals(0, genreRepository.count());
  }
}
