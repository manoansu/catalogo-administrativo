package pt.amane.application.category.create;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pt.amane.IntegrationTest;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.infrastructure.category.persistence.CategoryJpaEntity;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
class CreateCategoryUseCaseIT {

  @Autowired
  private CreateCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @MockitoBean
  private CategoryGateway categoryGateway;

  @Test
  void givenAValidCommand_whenCallsCreateCategory_shouldReturnCategoryId() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    Assertions.assertEquals(0, categoryRepository.count());

    final var aCategory =
        Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    Mockito.when(categoryGateway.create(any()))
        .thenReturn(aCategory);

    save(aCategory);

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Assertions.assertEquals(1, categoryRepository.count());

    final var actualCategory =
        categoryRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNull(actualCategory.getDeletedAt());

    verify(categoryGateway, times(1)).create(any());
  }

  @Test
  void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
    final String expectedName = null;
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    Assertions.assertEquals(0, categoryRepository.count());

    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Assertions.assertEquals(0, categoryRepository.count());

    Mockito.verify(categoryGateway, times(0)).create(any());
  }

  @Test
  void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;

    Assertions.assertEquals(0, categoryRepository.count());

    final var aCategory =
        Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    Mockito.when(categoryGateway.create(any()))
        .thenReturn(aCategory);

    save(aCategory);

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Assertions.assertEquals(1, categoryRepository.count());

    final var actualCategory =
        categoryRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertNotNull(actualCategory.getCreatedAt());
    Assertions.assertNotNull(actualCategory.getUpdatedAt());
    Assertions.assertNotNull(actualCategory.getDeletedAt());
  }

  @Test
  void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway error";

    final var aCommand =
        CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(categoryGateway).create(any());

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());
  }

  private void save(final Category... aCategory) {
    categoryRepository.saveAllAndFlush(
        Arrays.stream(aCategory)
            .map(CategoryJpaEntity::from)
            .toList()
    );
  }

}
