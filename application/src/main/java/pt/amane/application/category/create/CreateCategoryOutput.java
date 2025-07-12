package pt.amane.application.category.create;

import pt.amane.domain.category.Category;

public record CreateCategoryOutput(
    String id
) {

  //Factory method
  public static CreateCategoryOutput from(final String anId) {
    return new CreateCategoryOutput(anId);
  }

  public static CreateCategoryOutput from(final Category aCategory) {
    return from(aCategory.getId().getValue());
  }
}
