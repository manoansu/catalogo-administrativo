package pt.amane.infrastructure;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import pt.amane.MySQLGatewayTest;
import pt.amane.TestConfig;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.infrastructure.category.CategoryMySQLGateway;
import pt.amane.infrastructure.category.persistence.CategoryJpaEntity;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@MySQLGatewayTest
@Import(TestConfig.class)
class CategoryMySQLGatewayTest {

  @Autowired
  private CategoryMySQLGateway categoryGateway;

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void givenAValidCategory_whenCallsCreate_shouldReturnANewCategory() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais Assistida";
    final var expectedActive = true;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);


    Assertions.assertEquals(0, categoryRepository.count());

    final var actualCategory = categoryGateway.create(aCategory);

    Assertions.assertEquals(1, categoryRepository.count());

    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName,actualCategory.getName());
    Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
    Assertions.assertEquals(expectedActive,actualCategory.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(),actualCategory.getUpdatedAt());
    Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());

    final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

    Assertions.assertEquals(aCategory.getId().getValue(),actualEntity.getId());
    Assertions.assertEquals(expectedName,actualEntity.getName());
    Assertions.assertEquals(expectedDescription,actualEntity.getDescription());
    Assertions.assertEquals(expectedActive,actualEntity.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(),actualEntity.getCreatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(),actualEntity.getUpdatedAt());
    Assertions.assertEquals(aCategory.getDeletedAt(),actualEntity.getDeletedAt());
    Assertions.assertNull(actualEntity.getDeletedAt());
  }

  @Test
  void givenAValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated()
      throws InterruptedException {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais Assistida";
    final var expectedActive = true;

    final var aCategory = Category.newCategory("Film", null, expectedActive);


    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    Assertions.assertEquals(1, categoryRepository.count());

    final var actualInvalidEntity = categoryRepository.findById(aCategory.getId().getValue()).get();
    Assertions.assertEquals("Film",actualInvalidEntity.getName());
    Assertions.assertNull(actualInvalidEntity.getDescription());
    Assertions.assertEquals(expectedActive,actualInvalidEntity.isActive());

    // Avoid return false in test case
    Thread.sleep(15);
    final var aUpdatedCategory = aCategory.update(expectedName, expectedDescription, expectedActive);

    final var actualCategory = categoryGateway.update(aUpdatedCategory);


    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName,actualCategory.getName());
    Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
    Assertions.assertEquals(expectedActive,actualCategory.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
//    Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()));
    Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
    Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());

    final var actualEntity = categoryRepository.findById(aCategory.getId().getValue()).get();

    Assertions.assertEquals(aCategory.getId().getValue(),actualEntity.getId());
    Assertions.assertEquals(expectedName,actualEntity.getName());
    Assertions.assertEquals(expectedDescription,actualEntity.getDescription());
    Assertions.assertEquals(expectedActive,actualEntity.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(),actualEntity.getCreatedAt());
//    Assertions.assertTrue(aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt()));
    Assertions.assertEquals(aCategory.getUpdatedAt(), aUpdatedCategory.getUpdatedAt());
    Assertions.assertEquals(aCategory.getDeletedAt(),actualEntity.getDeletedAt());
    Assertions.assertNull(actualEntity.getDeletedAt());
  }

  @Test
  void givenAPrePersistedCategoryAndValidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {

    final var aCategory = Category.newCategory("Filmes", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    Assertions.assertEquals(1, categoryRepository.count());

    categoryGateway.deleteById(aCategory.getId());

    Assertions.assertEquals(0, categoryRepository.count());
  }

  @Test
  void givenInvalidCategoryId_whenTryToDeleteIt_shouldDeleteCategory() {


    Assertions.assertEquals(0, categoryRepository.count());

    categoryGateway.deleteById(CategoryID.from("invalid"));

    Assertions.assertEquals(0, categoryRepository.count());

  }

  @Test
  void givenAnIdWithNullValue_whenCallsDeleteById_shouldDoNothing() {
    final var expectedErrorMessage = "ID cannot be null";
    // Assuming categoryGateway.deleteById(null) delete a Category where id is null
    final var exception = Assertions.assertThrows(NullPointerException.class, () -> categoryGateway.deleteById(null));

    Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

  }

  @Test
  void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais Assistida";
    final var expectedActive = true;

    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedActive);


    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAndFlush(CategoryJpaEntity.from(aCategory));

    Assertions.assertEquals(1, categoryRepository.count());

    final var actualCategory = categoryGateway.findById(aCategory.getId()).get();


    Assertions.assertEquals(aCategory.getId(),actualCategory.getId());
    Assertions.assertEquals(expectedName,actualCategory.getName());
    Assertions.assertEquals(expectedDescription,actualCategory.getDescription());
    Assertions.assertEquals(expectedActive,actualCategory.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(),actualCategory.getCreatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.getUpdatedAt());
    Assertions.assertEquals(aCategory.getDeletedAt(),actualCategory.getDeletedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());

  }

  @Test
  void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {

    Assertions.assertEquals(0, categoryRepository.count());

    final var actualCategory = categoryGateway.findById(CategoryID.from("empty"));

    Assertions.assertTrue(actualCategory.isEmpty());

  }

  @Test
  void givenAnIdWithNullValue_whenCallsFindById_shouldDoNothing() {
    final var expectedErrorMessage = "ID cannot be null";
    // Assuming categoryGateway.findById(null) findById a Category where id is null
    final var exception = Assertions.assertThrows(NullPointerException.class, () -> categoryGateway.findById(null));

    Assertions.assertEquals(expectedErrorMessage, exception.getMessage());

  }

  @Test
  void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {

    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 3;

    final var filmes = Category.newCategory("Filmes", null, true);
    final var series = Category.newCategory("Séries", null, true);
    final var documentarios = Category.newCategory("Documentários", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    final var query = new SearchQuery(0,1,"","name","asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
  }

  @Test
  void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnEmptyPage() {

    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 0;

    Assertions.assertEquals(0, categoryRepository.count());

    final var query = new SearchQuery(0,1,"","name","asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(0, actualResult.items().size());

  }

  @Test
  void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {

    var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 3;

    final var filmes = Category.newCategory("Filmes", null, true);
    final var series = Category.newCategory("Séries", null, true);
    final var documentarios = Category.newCategory("Documentários", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    var query = new SearchQuery(0,1,"","name","asc");
    var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());

    // Page 1
    expectedPage = 1;
    query = new SearchQuery(1,1,"","name","asc");
    actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());

    // Page 2
    expectedPage = 2;
    query = new SearchQuery(2,1,"","name","asc");
    actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(series.getId(), actualResult.items().get(0).getId());
  }

  @Test
  void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_shouldReturnPaginated() {

    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 1;

    final var filmes = Category.newCategory("Filmes", null, true);
    final var series = Category.newCategory("Séries", null, true);
    final var documentarios = Category.newCategory("Documentários", null, true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    final var query = new SearchQuery(0,1,"doc","name","asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(documentarios.getId(), actualResult.items().get(0).getId());
  }

  @Test
  void givenPrePersistedCategoriesAndMaisAssistidaTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_shouldReturnPaginated() {

    final var expectedPage = 0;
    final var expectedPerPage = 1;
    final var expectedTotal = 1;

    final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
    final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
    final var documentarios = Category.newCategory("Documentários", "A categoria menos assistida", true);

    Assertions.assertEquals(0, categoryRepository.count());

    categoryRepository.saveAll(List.of(
        CategoryJpaEntity.from(filmes),
        CategoryJpaEntity.from(series),
        CategoryJpaEntity.from(documentarios)
    ));

    Assertions.assertEquals(3, categoryRepository.count());

    final var query = new SearchQuery(0,1,"MAIS ASSISTIDA","name","asc");
    final var actualResult = categoryGateway.findAll(query);

    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(expectedTotal, actualResult.total());
    Assertions.assertEquals(expectedPerPage, actualResult.items().size());
    Assertions.assertEquals(filmes.getId(), actualResult.items().get(0).getId());
  }
}
