package pt.amane.domain.castmember;

import java.util.List;
import java.util.Optional;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;

public interface CastMemberGateway {

  Castmember create(Castmember castmember);

  Castmember update(Castmember castmember);

  void delete(CastmemberID castmemberID);

  Optional<Castmember> findById(CastmemberID castmemberID);

  Pagination<Castmember> findAll(SearchQuery query);

  List<CastmemberID> existsByIds(Iterable<CastmemberID> castmemberID);


}
