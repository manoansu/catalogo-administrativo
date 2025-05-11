package pt.amane.application.category.retrieve.get;

import java.util.Objects;
import java.util.function.Supplier;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.exception.NotFoundException;

public non-sealed class DefaultGetCategoryByIdUseCase extends GetCategoryByIdUseCase{

  private final CategoryGateway categoryGateway;

  public DefaultGetCategoryByIdUseCase(CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Override
  public CategoryOutput execute(String anIn) {
    final var aCategoryID = CategoryID.from(anIn);
    return categoryGateway.findById(aCategoryID)
        .map(CategoryOutput::form)
        .orElseThrow(notFound(aCategoryID));
  }

  private static Supplier<? extends RuntimeException> notFound(CategoryID aCategoryID) {
    return () -> NotFoundException.with(Category.class, aCategoryID);
  }
}
