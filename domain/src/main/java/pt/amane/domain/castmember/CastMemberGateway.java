package pt.amane.domain.castmember;

import java.util.List;
import java.util.Optional;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

public interface CastMemberGateway {

  CastMember create(CastMember castmember);

  CastMember update(CastMember castmember);

  void deleteById(CastMemberID castmemberID);

  Optional<CastMember> findById(CastMemberID castmemberID);

  Pagination<CastMember> findAll(SearchQuery query);

  List<CastMemberID> existsByIds(Iterable<CastMemberID> castmemberID);


}
