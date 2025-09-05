package pt.amane.application.category.retrieve.list;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

class ListCategoriesUseCaseTest extends UseCaseTest {

  @InjectMocks
  private ListCategoriesUseCaseImpl useCase;

  @Mock
  private CategoryGateway categoryGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(categoryGateway);
  }

  @Test
  void givenAValidQuery_whenCallsListCategories_thenShouldReturnCategories() {

   //given
    final var categories = List.of(
        Category.newCategory("Filmes", null, true),
        Category.newCategory("Series", null, true)
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    //when
    final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

    final var expectedItemCount = 2;
    final var expectedResul  = expectedPagination.map(CategoryListOutput::from);

    Mockito.when(categoryGateway.findAll(aQuery))
        .thenReturn(expectedPagination);

    final var actualResult = useCase.execute(aQuery);

    //then
    Assertions.assertEquals(expectedItemCount, actualResult.items().size());
    Assertions.assertEquals(expectedResul, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(categories.size(), actualResult.total());
  }

  @Test
  void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {

   //given
    final var categories = List.<Category>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    //when
    final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);

    final var expectedItemCount = 0;
    final var expectedResul  = expectedPagination.map(CategoryListOutput::from);

    Mockito.when(categoryGateway.findAll(aQuery))
        .thenReturn(expectedPagination);

    final var actualResult = useCase.execute(aQuery);

    //then
    Assertions.assertEquals(expectedItemCount, actualResult.items().size());
    Assertions.assertEquals(expectedResul, actualResult);
    Assertions.assertEquals(expectedPage, actualResult.currentPage());
    Assertions.assertEquals(expectedPerPage, actualResult.perPage());
    Assertions.assertEquals(categories.size(), actualResult.total());

  }

  @Test
  void givenAValidQuery_whenGatewayThrowsException_shouldReturnException() {
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedErrorMessage = "Gateway error";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    when(categoryGateway.findAll(eq(aQuery)))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

    final var actualException =
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(aQuery));

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
  }

}