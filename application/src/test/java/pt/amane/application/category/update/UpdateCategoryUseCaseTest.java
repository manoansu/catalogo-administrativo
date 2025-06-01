package pt.amane.application.category.update;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
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

class UpdateCategoryUseCaseTest extends UseCaseTest {

  @InjectMocks
  private UpdateCategoryUseCaseImpl useCase;

  @Mock
  private CategoryGateway categoryGateway;


  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  //TDD
  // Happy path text
  @Test
  void givenAValidCommand_whenCallUpdateCategory_shouldReturnCategoryId() {

    //given
    final var aCategory = Category.newCategory("Filmes", null, true);

    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();

    final var aCommand = UpdateCategoryCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedDescription,
        expectedIsActive);

    // When
    Mockito.when(categoryGateway.findById(expectedId))
            .thenReturn(Optional.of(Category.with(aCategory)));

    Mockito.when(categoryGateway.update(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    final var actualOutput = useCase.execute(aCommand).get();

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    Mockito.verify(categoryGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));

    Mockito.verify(categoryGateway, Mockito.times(1)).update(Mockito.argThat(updatedCategory ->
        Objects.equals(expectedName, updatedCategory.getName())
            && Objects.nonNull(updatedCategory.getId())
            && Objects.equals(expectedDescription, updatedCategory.getDescription())
            && Objects.equals(expectedIsActive, updatedCategory.isActive())
            && Objects.nonNull(updatedCategory.getCreatedAt())
            && Objects.nonNull(updatedCategory.getUpdatedAt())
            && Objects.isNull(updatedCategory.getDeletedAt())
    ));
  }

  //Passing an invalid property (name) test
  @Test
  public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {

    //given
    final var aCategory = Category.newCategory("Filmes", null, true);

    final String expectedName = null;
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();

    final var expectedErrorMessage = "'name' should not be null";
    final var expectedErrorCount = 1;

    // When
    Mockito.when(categoryGateway.findById(expectedId))
        .thenReturn(Optional.of(Category.with(aCategory)));

    final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    // then
    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

    Mockito.verify(categoryGateway, Mockito.times(0)).update(Mockito.any());

  }

  // Updating an inactive category test
  @Test
  public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {

    //given
    final var aCategory =
        Category.newCategory("Film", null, true);


    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;
    final var expectedId = aCategory.getId();

    final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    // When
    Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
        .thenReturn(Optional.of(Category.with(aCategory)));

    Mockito.when(categoryGateway.update(Mockito.any()))
        .thenAnswer(returnsFirstArg());

    // then
    assertTrue(aCategory.isActive());
    assertNull(aCategory.getDeletedAt());

    final var actualOutput = useCase.execute(aCommand).get();

    // then
    assertNotNull(actualOutput);
    assertNotNull(actualOutput.id());

    verify(categoryGateway, times(1)).findById(Mockito.eq(expectedId));

    verify(categoryGateway, times(1)).update(argThat(updatedCategory ->
        Objects.equals(expectedName, updatedCategory.getName())
            && Objects.nonNull(updatedCategory.getId())
            && Objects.equals(expectedDescription, updatedCategory.getDescription())
            && Objects.equals(expectedIsActive, updatedCategory.isActive())
            && Objects.nonNull(updatedCategory.getCreatedAt())
            && Objects.nonNull(updatedCategory.getUpdatedAt())
            && Objects.nonNull(updatedCategory.getDeletedAt())
    ));
  }

  //Simulating a generic error coming from the gateway test
  @Test
  public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {

    //given
    final var aCategory =
        Category.newCategory("Film", null, true);

    final String expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;
    final var expectedId = aCategory.getId();

    final var expectedErrorMessage = "Gateway error";
    final var expectedErrorCount = 1;

    final var aCommand = UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

    // When
    Mockito.when(categoryGateway.findById(Mockito.eq(expectedId)))
            .thenReturn(Optional.of(Category.with(aCategory)));

    Mockito.when(categoryGateway.update(Mockito.any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var notification = useCase.execute(aCommand).getLeft();

    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

    Mockito.verify(categoryGateway, times(1)).update(argThat(
        aUpdatedCategory ->
            Objects.equals(expectedName, aUpdatedCategory.getName())
                && Objects.equals(expectedDescription, aUpdatedCategory.getDescription())
                && Objects.equals(expectedIsActive, aUpdatedCategory.isActive())
                && Objects.equals(expectedId, aUpdatedCategory.getId())
                && Objects.equals(aCategory.getCreatedAt(), aUpdatedCategory.getCreatedAt())
                && aCategory.getUpdatedAt().isBefore(aUpdatedCategory.getUpdatedAt())
                && Objects.isNull(aUpdatedCategory.getDeletedAt())
    ));

  }

  //Update category passing invalid Id.
  @Test
  public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {

    //given
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

    //when
    when(categoryGateway.findById(eq(CategoryID.from(expectedId))))
        .thenReturn(Optional.empty());

    final var actualException =
        Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

    //then
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    Mockito.verify(categoryGateway, times(1)).findById(eq(CategoryID.from(expectedId)));

    Mockito.verify(categoryGateway, times(0)).update(any());

  }

}