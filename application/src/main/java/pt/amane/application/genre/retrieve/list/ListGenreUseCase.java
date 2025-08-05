package pt.amane.application.genre.retrieve.list;

import pt.amane.UseCase;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

public sealed abstract class ListGenreUseCase
    extends UseCase<SearchQuery, Pagination<GenreListOutput>>
    permits ListGenreUseCaseImpl {

}
