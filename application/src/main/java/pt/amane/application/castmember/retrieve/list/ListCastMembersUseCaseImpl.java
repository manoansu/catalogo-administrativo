package pt.amane.application.castmember.retrieve.list;

import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.domain.validation.ObjectsValidator;

public class ListCastMembersUseCaseImpl extends ListCastMembersUseCase {

  private final CastMemberGateway castMemberGateway;

  public ListCastMembersUseCaseImpl(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
  }

  @Override
  public Pagination<ListCastMembersOutput> execute(SearchQuery aQuery) {
    return castMemberGateway.findAll(aQuery)
        .map(ListCastMembersOutput::from);
  }
}
