package pt.amane.domain.castmember;

import java.util.List;
import java.util.Optional;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

public interface CastMemberGateway {

  CastMember create(CastMember castmember);

  CastMember update(CastMember castmember);

  void deleteById(CastmemberID castmemberID);

  Optional<CastMember> findById(CastmemberID castmemberID);

  Pagination<CastMember> findAll(SearchQuery query);

  List<CastmemberID> existsByIds(Iterable<CastmemberID> castmemberID);


}
