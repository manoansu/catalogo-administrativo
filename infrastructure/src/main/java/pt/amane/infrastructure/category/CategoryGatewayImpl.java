package pt.amane.infrastructure.category;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.infrastructure.category.persistence.CategoryJpaEntity;
import pt.amane.infrastructure.category.persistence.CategoryRepository;
import pt.amane.infrastructure.utils.SpecificationUtils;

@Component
public class CategoryGatewayImpl implements CategoryGateway {

  private final CategoryRepository categoryRepository;

  public CategoryGatewayImpl(final CategoryRepository categoryRepository) {
    this.categoryRepository = (CategoryRepository) ObjectsValidator.objectValidation(categoryRepository);
  }

  @Override
  public Category create(Category aCategory) {
    return save(aCategory);
  }

  @Override
  public void deleteById(CategoryID anId) {
    ObjectsValidator.objectValidation(anId);
    categoryRepository.deleteById(anId.getValue());
  }

  @Override
  public Optional<Category> findById(CategoryID anId) {
    ObjectsValidator.objectValidation(anId);
    return categoryRepository.findById(anId.getValue())
        .map(CategoryJpaEntity::toAggregate);
  }

  @Override
  public Category update(Category aCategory) {
    return save(aCategory);
  }

  @Override
  public Pagination<Category> findAll(SearchQuery aQuery) {
    //Pagination
    final var page = PageRequest.of(
        aQuery.page(),
        aQuery.perPage(),
        Sort.by(Direction.fromString(aQuery.direction()), aQuery.sort()));

    //Dynamic search by creative terms (name or description)
    final var specifications = Optional.ofNullable(aQuery.terms())
        .filter(str -> !str.isEmpty())
        .map(str -> {
          final Specification<CategoryJpaEntity> nameLike = SpecificationUtils.like("name", str);
          final Specification<CategoryJpaEntity> descriptionLike = SpecificationUtils.like("description", str);
          return nameLike.or(descriptionLike);
        })
        .orElse(null);

    final var pageRequest = this.categoryRepository.findAll(Specification.where(specifications), page);

    return new Pagination<>(
        pageRequest.getNumber(),
        pageRequest.getSize(),
        pageRequest.getTotalElements(),
        pageRequest.map(CategoryJpaEntity::toAggregate).toList());
    }

  @Override
  public List<CategoryID> existsByIds(Iterable<CategoryID> categoryIDs) {

    //convert Iterable to stream there is the StreamSupport method that has two parameters
    final var ids = StreamSupport.stream(categoryIDs.spliterator(), false)
        .map(CategoryID::getValue)
        .toList();

    return this.categoryRepository.existsByIds(ids)
        .stream()
        .map(CategoryID::from).toList();
  }

  private Category save(Category aCategory) {
    return categoryRepository.save(CategoryJpaEntity
        .from(aCategory)).toAggregate();
  }
}
