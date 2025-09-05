package pt.amane.application.category.retrieve.list;

import java.time.Instant;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryID;

public record CategoryListOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    Instant createdAt,
    Instant deletedAt
) {

  //Factory method
  public static CategoryListOutput from(final Category aCategory) {
    return new CategoryListOutput(
        aCategory.getId(),
        aCategory.getName(),
        aCategory.getDescription(),
        aCategory.isActive(),
        aCategory.getCreatedAt(),
        aCategory.getDeletedAt()
    );
  }
}
