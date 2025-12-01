package pt.amane.application.category.retrieve.list;

import pt.amane.UseCase;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

/**
 * In java world, you always work for abstration, not for implmentation,
 * because when this class has a new feature , you can only change the implementation not abstration.
 */
public abstract class ListCategoriesUseCase
    extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {

}
