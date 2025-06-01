package pt.amane.application.category.delete;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;

class DeleteCategoryUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DeleteCategoryUseCaseImpl useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  @Test
  void givenAValidId_whenCallsDeleteCategory_shouldBeOK() {

    //given
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
    final var expectedId = aCategory.getId();

    //when
    Mockito.doNothing().when(categoryGateway)
        .deleteById(Mockito.eq(expectedId));

    //then
    Assertions.assertDoesNotThrow(() ->
        useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
  }

  @Test
  public void givenAInvalidId_whenCallsDeleteCategory_shouldBeOK() {

    //given
    final var expectedId = CategoryID.from("123");

    //when
    Mockito.doNothing().when(categoryGateway)
        .deleteById(Mockito.eq(expectedId));

    //then
    Assertions.assertDoesNotThrow(() ->
        useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
  }

  @Test
  public void givenAValidId_whenGatewayThrowsException_shouldReturnException() {

    //given
    final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
    final var expectedId = aCategory.getId();

    //when
    Mockito.doThrow(new IllegalStateException("Gateway error"))
        .when(categoryGateway).deleteById(Mockito.eq(expectedId));

    //then
    Assertions.assertThrows(IllegalStateException.class, () ->
        useCase.execute(expectedId.getValue()));

    Mockito.verify(categoryGateway, Mockito.times(1)).deleteById(Mockito.eq(expectedId));
  }

}