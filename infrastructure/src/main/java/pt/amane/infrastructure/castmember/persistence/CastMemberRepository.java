package pt.amane.infrastructure.castmember.persistence;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CastMemberRepository extends JpaRepository<CastMemberJpaEntity, String> {

  Page<CastMemberJpaEntity> findAll(Specification<CastMemberJpaEntity> specification, Pageable page);

  List<String> existsByIdIn(List<String> listIds);

}
