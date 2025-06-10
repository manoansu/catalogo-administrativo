package pt.amane.application.castmember.retrieve.list;

import pt.amane.UseCase;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

public sealed abstract class ListCastMembersUseCase
    extends UseCase<SearchQuery, Pagination<ListCastMembersOutput>>
    permits ListCastMembersUseCaseImpl {

}
