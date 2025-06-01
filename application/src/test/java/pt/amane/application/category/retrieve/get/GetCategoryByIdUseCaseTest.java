package pt.amane.application.category.retrieve.get;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotFoundException;

class GetCategoryByIdUseCaseTest extends UseCaseTest {

  @InjectMocks
  private GetCategoryByIdUseCaseImpl useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  @Test
  public void givenAValidId_whenCallsGetCategory_shouldReturnCategory() {

    //given
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    //when
    final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

    final var expectedId = aCategory.getId();

    Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
        .thenReturn(Optional.of(Category.with(aCategory)));

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
  public void givenAInvalidId_whenCallsGetCategory_shouldReturnNotFound() {

    //given
    final var expectedErrorMessage = "Category with ID 123 was not found";
    final var expectedId = CategoryID.from("123");

    //when
    Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
        .thenReturn(Optional.empty());

    final var actualException = Assertions.assertThrows(NotFoundException.class,
        () -> useCase.execute(expectedId.getValue()));

    //then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  }

  @Test
  public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {

   //given
    final var expectedErrorMessage = "Gateway error";
    final var expectedId = CategoryID.from("123");

    //when
    Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException = Assertions.assertThrows(IllegalStateException.class,
        () -> useCase.execute(expectedId.getValue()));

    //then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  }
}