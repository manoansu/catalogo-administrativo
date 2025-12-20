package pt.amane.e2e.category;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import pt.amane.E2ETest;
import pt.amane.domain.category.CategoryID;
import pt.amane.e2e.E2ETestListener;
import pt.amane.e2e.MockDsl;
import pt.amane.e2e.MySQLCleanUpExtension;
import pt.amane.infrastructure.category.model.UpdateCategoryRequest;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@E2ETest
@Testcontainers
@ExtendWith(MySQLCleanUpExtension.class)
public class CategoryE2ETest extends E2ETestListener implements MockDsl{

  @Autowired
  private MockMvc mvc;

  @Autowired
  private CategoryRepository categoryRepository;

  @BeforeEach
  @Transactional
  void clean() {
    categoryRepository.deleteAll();
  }

  @Override
  public MockMvc mvc() {
    return this.mvc;
  }

  @Test
  void asACatalogAdminIShouldBeAbleToCreateANewCategoryWithValidValues() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

    final var actualCategory = retrieveACategory(actualId);

    Assertions.assertEquals(expectedName, actualCategory.name());
    Assertions.assertEquals(expectedDescription, actualCategory.description());
    Assertions.assertEquals(expectedIsActive, actualCategory.active());
    Assertions.assertNotNull(actualCategory.createdAt());
    Assertions.assertNotNull(actualCategory.updatedAt());
    Assertions.assertNull(actualCategory.deletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToNavigateToAllCategories() throws Exception {
    givenACategory("Filmes", null, true);
    givenACategory("Documentários", null, true);
    givenACategory("Séries", null, true);

    listCategories(0, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")));

    listCategories(1, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(1)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));

    listCategories(2, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(2)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Séries")));

    listCategories(3, 1)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(3)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(0)));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToSearchBetweenAllCategories() throws Exception {
    givenACategory("Filmes", null, true);
    givenACategory("Documentários", null, true);
    givenACategory("Séries", null, true);

    listCategories(0, 1, "fil")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(1)))
        .andExpect(jsonPath("$.total", equalTo(1)))
        .andExpect(jsonPath("$.items", hasSize(1)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Filmes")));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToSortAllCategoriesByDescriptionDesc() throws Exception {
    givenACategory("Filmes", "C", true);
    givenACategory("Documentários", "Z", true);
    givenACategory("Séries", "A", true);

    listCategories(0, 3, "", "description", "desc")
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.current_page", equalTo(0)))
        .andExpect(jsonPath("$.per_page", equalTo(3)))
        .andExpect(jsonPath("$.total", equalTo(3)))
        .andExpect(jsonPath("$.items", hasSize(3)))
        .andExpect(jsonPath("$.items[0].name", equalTo("Documentários")))
        .andExpect(jsonPath("$.items[1].name", equalTo("Filmes")))
        .andExpect(jsonPath("$.items[2].name", equalTo("Séries")));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToGetACategoryByItsIdentifier() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var actualId = givenACategory(expectedName, expectedDescription, expectedIsActive);

    final var actualCategory = retrieveACategory(actualId);

    Assertions.assertEquals(expectedName, actualCategory.name());
    Assertions.assertEquals(expectedDescription, actualCategory.description());
    Assertions.assertEquals(expectedIsActive, actualCategory.active());
    Assertions.assertNotNull(actualCategory.createdAt());
    Assertions.assertNotNull(actualCategory.updatedAt());
    Assertions.assertNull(actualCategory.deletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToSeeATreatedErrorByGettingANotFoundCategory() throws Exception {
    final var aRequest = MockMvcRequestBuilders.get("/categories/123")
//                .with(ApiTest.ADMIN_JWT)
        .accept(MediaType.APPLICATION_JSON)
        .contentType(MediaType.APPLICATION_JSON);

    this.mvc.perform(aRequest)
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message", equalTo("Category with ID 123 was not found")));
  }

  @Test
  void asACatalogAdminIShouldBeAbleToUpdateACategoryByItsIdentifier() throws Exception {
    final var actualId = givenACategory("Movies", null, true);

    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

    updateACategory(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToInactivateACategoryByItsIdentifier() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;

    final var actualId = givenACategory(expectedName, expectedDescription, true);

    final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

    updateACategory(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNotNull(actualCategory.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToActivateACategoryByItsIdentifier() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var actualId = givenACategory(expectedName, expectedDescription, false);

    final var aRequestBody = new UpdateCategoryRequest(expectedName, expectedDescription, expectedIsActive);

    updateACategory(actualId, aRequestBody)
        .andExpect(status().isOk());

    final var actualCategory = categoryRepository.findById(actualId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());
  }

  @Test
  void asACatalogAdminIShouldBeAbleToDeleteACategoryByItsIdentifier() throws Exception {
    final var actualId = givenACategory("Filmes", null, true);

    deleteACategory(actualId)
        .andExpect(status().isNoContent());

    Assertions.assertFalse(this.categoryRepository.existsById(actualId.getValue()));
  }

  @Test
  void asACatalogAdminIShouldNotSeeAnErrorByDeletingANotExistentCategory() throws Exception {
    deleteACategory(CategoryID.from("12313"))
        .andExpect(status().isNoContent());

    Assertions.assertEquals(0, categoryRepository.count());
  }
}
