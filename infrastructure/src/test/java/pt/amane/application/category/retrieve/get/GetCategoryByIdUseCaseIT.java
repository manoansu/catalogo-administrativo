package pt.amane.application.category.retrieve.get;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

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
import pt.amane.infrastructure.category.persistence.CategoryJpaEntity;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@IntegrationTest
class GetCategoryByIdUseCaseIT {

  @Autowired
  private GetCategoryByIdUseCase useCase;

  @Autowired
  private CategoryRepository categoryRepository;

   @MockitoBean
  private CategoryGateway categoryGateway;

  @Test
  void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {

    //given
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    //when
    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    final var expectedId = aCategory.getId();

    save(aCategory);

    doReturn(Optional.of(aCategory)).when(categoryGateway).findById(expectedId);

    final var actualCategory = useCase.execute(expectedId.getValue());

    //then
    Assertions.assertEquals(expectedId, actualCategory.id());
    Assertions.assertEquals(expectedName, actualCategory.name());
    Assertions.assertEquals(expectedDescription, actualCategory.description());
    Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
    Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.ceatedAt());
    Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
    Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
  }

  @Test
  void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {

    //given
    final var expectedErrorMessage = "Category with ID 123 was not found";
    final var expectedId = CategoryID.from("123");

    doReturn(Optional.empty()).when(categoryGateway).findById(expectedId);

    //when
    final var actualException = Assertions.assertThrows(NotFoundException.class,
        () -> useCase.execute(expectedId.getValue()));

    //then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  }

  @Test
  void givenAValidId_whenGatewayThrowsException_shouldReturnException() {

    //given
    final var expectedErrorMessage = "Gateway error";
    final var expectedId = CategoryID.from("123");

    //when
    doThrow(new IllegalStateException(expectedErrorMessage))
        .when(categoryGateway).findById(Mockito.eq(expectedId));

    final var actualException = Assertions.assertThrows(IllegalStateException.class,
        () -> useCase.execute(expectedId.getValue()));

    //then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  }

  private void save(final Category... aCategory) {
     categoryRepository.saveAllAndFlush(
         Arrays.stream(aCategory)
             .map(CategoryJpaEntity::from)
             .toList());
  }

}
