package pt.amane.application.category.create;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.CategoryGateway;

class CreateCategoryUseCaseTest extends UseCaseTest {

  @InjectMocks
  private CreateCategoryUseCaseImpl useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  //TDD
  // Happy path text
  @Test
  void givenAValidCommand_whenCallCreatedCategory_shouldReturnCategoryId() {

    //given
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    // When create an instantiation for category, then return the first params of category.
    Mockito.when(categoryGateway.create(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(categoryGateway, Mockito.times(1)).create(Mockito.argThat(aCategory ->
      Objects.equals(expectedName, aCategory.getName())
          && Objects.nonNull(aCategory.getId())
          && Objects.equals(expectedDescription, aCategory.getDescription())
          && Objects.equals(expectedIsActive, aCategory.isActive())
          && Objects.nonNull(aCategory.getCreatedAt())
          && Objects.nonNull(aCategory.getUpdatedAt())
          && Objects.isNull(aCategory.getDeletedAt())
    ));
  }

  //Passing an invalid property (name) test
  @Test
  void givenAInvalidName_whenCallCreatedCategory_shouldReturnDomainException() {

    //given
    final String expectedName = null;
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    // When
    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    // then
    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

    Mockito.verify(categoryGateway, Mockito.times(0)).create(Mockito.any());

  }

  // Creating an inactive category test
  @Test
  void givenAValidCommandWithInactiveCategory_whenCallCreatedCategory_shouldReturnInactiveCategoryId() {

    //given
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    // When create an instantiation for category, then return the first params of category.
    Mockito.when(categoryGateway.create(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    // then
    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    verify(categoryGateway, times(1)).create(argThat(aCategory ->
        Objects.equals(expectedName, aCategory.getName())
            && Objects.nonNull(aCategory.getId())
            && Objects.equals(expectedDescription, aCategory.getDescription())
            && Objects.equals(expectedIsActive, aCategory.isActive())
            && Objects.nonNull(aCategory.getCreatedAt())
            && Objects.nonNull(aCategory.getUpdatedAt())
            && Objects.nonNull(aCategory.getDeletedAt())
    ));
  }

  //Simulating a generic error coming from the gateway test
  @Test
  void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAnException() {

    //given
    final String expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedErrorMessage = "Gateway error";
    final var expectedErrorCount = 1;

    final var aCommand = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

    // When create an instantiation for category, then return the first params of category.
    Mockito.when(categoryGateway.create(Mockito.any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    verify(categoryGateway, times(1)).create(argThat(aCategory ->
        Objects.equals(expectedName, aCategory.getName())
            && Objects.nonNull(aCategory.getId())
            && Objects.equals(expectedDescription, aCategory.getDescription())
            && Objects.equals(expectedIsActive, aCategory.isActive())
            && Objects.nonNull(aCategory.getCreatedAt())
            && Objects.nonNull(aCategory.getUpdatedAt())
            && Objects.isNull(aCategory.getDeletedAt())

    ));

  }

}