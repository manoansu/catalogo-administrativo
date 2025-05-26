package pt.amane.infrastructure.category.presenters;

import pt.amane.application.category.retrieve.get.CategoryOutput;
import pt.amane.infrastructure.category.model.CategoryListResponse;
import pt.amane.infrastructure.category.model.CategoryResponse;

public interface CategoryApiPresenter {

  static CategoryResponse present(final CategoryOutput output) {
    return new CategoryResponse(
        output.id().getValue(),
        output.name(),
        output.description(),
        output.isActive(),
        output.ceatedAt(),
        output.updatedAt(),
        output.deletedAt()
    );
  }

  static CategoryListResponse present(final CategoryListResponse listResponse) {
    return new CategoryListResponse(
        listResponse.id(),
        listResponse.name(),
        listResponse.description(),
        listResponse.active(),
        listResponse.createdAt(),
        listResponse.deletedAt()
    );
  }

}
