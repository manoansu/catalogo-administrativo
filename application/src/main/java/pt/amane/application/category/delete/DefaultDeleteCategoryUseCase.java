package pt.amane.application.category.delete;

import java.util.Objects;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;

public non-sealed class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {

  private final CategoryGateway categoryGateway;

  public DefaultDeleteCategoryUseCase(final CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Override
  public void execute(String anIn) {
    this.categoryGateway.deleteById(CategoryID.from(anIn));
  }
}
