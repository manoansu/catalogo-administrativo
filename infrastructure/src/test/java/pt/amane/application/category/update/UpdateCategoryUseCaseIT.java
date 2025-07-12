package pt.amane.application.category.update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pt.amane.IntegrationTest;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.utils.InstantUtils;
import pt.amane.infrastructure.category.persistence.CategoryJpaEntity;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
class UpdateCategoryUseCaseIT {

  @Autowired
  private UpdateCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @MockitoBean
  private CategoryGateway categoryGateway;

  @Test
  void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
    final var aCategory =
        Category.newCategory("Film", null, true);

    save(aCategory);

    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();

    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var category = Category.with(
        aCategory.getId(),
        aCommand.name(),
        aCommand.description(),
        aCommand.isActive(),
        InstantUtils.now(),
        InstantUtils.now(),
        null);

    Assertions.assertEquals(1, categoryRepository.count());

    Mockito.when(categoryGateway.findById(any()))
        .thenReturn(Optional.of(category));

    Mockito.when(categoryGateway.update(any()))
        .thenReturn(category);

    save(category);

    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());


    final var actualCategory =
        categoryRepository.findById(actualOutput.id()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertEquals(category.getCreatedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    Assertions.assertEquals(category.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
    Assertions.assertNull(actualCategory.getDeletedAt());

    Mockito.verify(categoryGateway, times(1)).findById(any());
    Mockito.verify(categoryGateway, times(1)).update(any());
  }

  @Test
  void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
    final var aCategory =
        Category.newCategory("Film", null, true);

    save(aCategory);

    final String expectedName = null;
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();

    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    Mockito.when(categoryGateway.findById(any()))
        .thenReturn(Optional.of(aCategory));

    final var aCommand =
        UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(categoryGateway, times(0)).update(any());
  }

  @Test
  void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
    final var aCategory =
        Category.newCategory("Film", null, true);

    save(aCategory);

    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;
    final var expectedId = aCategory.getId();

    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var category = Category.with(
        CategoryID.from(aCommand.id()),
        aCommand.name(),
        aCommand.description(),
        aCommand.isActive(),
        InstantUtils.now(),
        InstantUtils.now(),
        InstantUtils.now()
    );

    Assertions.assertTrue(aCategory.isActive());
    Assertions.assertNull(aCategory.getDeletedAt());

    Mockito.when(categoryGateway.findById(any()))
        .thenReturn(Optional.of(aCategory));

    Mockito.when(categoryGateway.update(any()))
        .thenReturn(aCategory);

    save(category);
    final var actualOutput = useCase.execute(aCommand).get();

    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    final var actualCategory =
        categoryRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedName, actualCategory.getName());
    Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertEquals(category.getCreatedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    Assertions.assertEquals(category.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
    Assertions.assertNotNull(actualCategory.getDeletedAt());

    Mockito.verify(categoryGateway, times(1)).findById(any());
    Mockito.verify(categoryGateway, times(1)).update(any());
  }

  @Test
  void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
    final var aCategory =
        Category.newCategory("Film", null, true);

    save(aCategory);

    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "Gateway error";

    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var category = Category.with(
        CategoryID.from(aCommand.id()),
        aCommand.name(),
        aCommand.description(),
        aCommand.isActive(),
        InstantUtils.now(),
        InstantUtils.now(),
        InstantUtils.now()
    );

    Mockito.when(categoryGateway.findById(any()))
        .thenReturn(Optional.of(aCategory));

    Mockito.when(categoryGateway.update(any()))
        .thenReturn(category);

    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(categoryGateway).update(any());

    save(category);

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    final var actualCategory =
        categoryRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
    Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
    Assertions.assertEquals(aCategory.isActive(), actualCategory.isActive());
    Assertions.assertEquals(category.getCreatedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getCreatedAt().truncatedTo(ChronoUnit.SECONDS));
    Assertions.assertEquals(category.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getUpdatedAt().truncatedTo(ChronoUnit.SECONDS));
    Assertions.assertEquals(category.getDeletedAt().truncatedTo(ChronoUnit.SECONDS), actualCategory.getDeletedAt().truncatedTo(ChronoUnit.SECONDS));

    Mockito.verify(categoryGateway, times(1)).findById(any());
    Mockito.verify(categoryGateway, times(1)).update(any());
  }

  @Test
  void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;
    final var expectedId = "123";
    final var expectedErrorMessage = "Category with ID 123 was not found";

    final var aCommand = UpdateCategoryCommand.with(
        expectedId,
        expectedName,
        expectedDescription,
        expectedIsActive
    );

    final var actualException =
        Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }

  private void save(final Category... aCategory) {
    categoryRepository.saveAllAndFlush(
        Arrays.stream(aCategory)
            .map(CategoryJpaEntity::from)
            .toList()
    );
  }
}
