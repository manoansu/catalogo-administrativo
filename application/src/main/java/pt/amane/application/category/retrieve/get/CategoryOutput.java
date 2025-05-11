package pt.amane.application.category.retrieve.get;

import java.time.Instant;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryID;

public record CategoryOutput(
    CategoryID id,
    String name,
    String description,
    boolean isActive,
    Instant ceatedAt,
    Instant updatedAt,
    Instant deletedAt
) {

  public static CategoryOutput form(final Category aCategory) {
    return new CategoryOutput(
        aCategory.getId(),
        aCategory.getName(),
        aCategory.getDescription(),
        aCategory.isActive(),
        aCategory.getCreatedAt(),
        aCategory.getUpdatedAt(),
        aCategory.getDeletedAt());
  }

}
