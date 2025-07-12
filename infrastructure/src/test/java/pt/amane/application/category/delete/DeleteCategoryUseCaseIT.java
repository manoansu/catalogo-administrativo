package pt.amane.application.category.delete;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;

import java.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pt.amane.IntegrationTest;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.infrastructure.category.persistence.CategoryJpaEntity;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
class DeleteCategoryUseCaseIT {

  @Autowired
  private DeleteCategoryUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

  @MockitoBean
  private CategoryGateway categoryGateway;

  @Test
  void  givenAValidId_whenCallsDeleteCategory_shouldBeOK() {

    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
    final var expectedId = aCategory.getId();

    save(aCategory);

    Assertions.assertEquals(1, categoryRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    delete(aCategory);

    Assertions.assertEquals(0, categoryRepository.count());
  }

  @Test
  void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {
    final var expectedId = CategoryID.from("123");

    Assertions.assertEquals(0, categoryRepository.count());

    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    Assertions.assertEquals(0, categoryRepository.count());
  }

  @Test
  void givenAValidId_whenGatewayThrowsException_shouldReturnException() {
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
    final var expectedId = aCategory.getId();

    doThrow(new IllegalStateException("Gateway error"))
        .when(categoryGateway).deleteById(eq(expectedId));

    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, times(1)).deleteById(eq(expectedId));
  }

  private void save(final Category... aCategory) {
    categoryRepository.saveAllAndFlush(
        Arrays.stream(aCategory)
            .map(CategoryJpaEntity::from)
            .toList()
    );
  }

  private void delete(final Category... aCategory) {
    categoryRepository.deleteAll(
        Arrays.stream(aCategory)
            .map(CategoryJpaEntity::from)
            .toList()
    );
  }

}
