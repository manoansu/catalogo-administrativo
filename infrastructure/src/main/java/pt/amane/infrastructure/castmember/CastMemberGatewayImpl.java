package pt.amane.infrastructure.castmember;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.infrastructure.castmember.persistence.CastMemberJpaEntity;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;
import pt.amane.infrastructure.utils.SpecificationUtils;

@Component
public class CastMemberGatewayImpl implements CastMemberGateway {

  private final CastMemberRepository castMemberRepository;

  @Autowired
  public CastMemberGatewayImpl(final CastMemberRepository castMemberRepository) {
    this.castMemberRepository = (CastMemberRepository) ObjectsValidator.objectValidation(castMemberRepository);
  }

  @Override
  public CastMember create(CastMember castmember) {
    return save(castmember);
  }

  @Override
  public CastMember update(CastMember castmember) {
    return save(castmember);
  }

  @Override
  public void deleteById(CastMemberID castmemberID) {
    final var anId = castmemberID.getValue();
    if (castMemberRepository.existsById(anId)) {
      castMemberRepository.deleteById(anId);
    }
  }

  @Override
  public Optional<CastMember> findById(CastMemberID castmemberID) {
    return castMemberRepository.findById(castmemberID.getValue())
        .map(CastMemberJpaEntity::toAggregate);
  }

  @Override
  public Pagination<CastMember> findAll(SearchQuery aQuery) {
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
    );

    final var where = Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isBlank())
        .map(this::assembleSpecification)
        .orElse(null);

    final var pageResult =
        this.castMemberRepository.findAll(where, page);

    return new Pagination<>(
        pageResult.getNumber(),
        pageResult.getSize(),
        pageResult.getTotalElements(),
        pageResult.map(CastMemberJpaEntity::toAggregate).toList()
    );
  }

  @Override
  public List<CastMemberID> existsByIds(Iterable<CastMemberID> castMemberIDS) {
    final var ids = StreamSupport.stream(castMemberIDS.spliterator(), false)
        .map(CastMemberID::getValue)
        .toList();
    return this.castMemberRepository.idsThatExist(ids).stream()
        .map(CastMemberID::from)
        .toList();
  }

  private CastMember save(CastMember castmember) {
    return castMemberRepository.save(CastMemberJpaEntity.from(castmember)).toAggregate();
  }

  private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
    return SpecificationUtils.like("name", terms);
  }

}
