package pt.amane.infrastructure.castmember.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

  Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> specification, Pageable page);

  @Query(value = "SELECT c.id FROM CastMember c WHERE c.id IN :ids")
  List<String> idsThatExist(List<String> ids);

}
