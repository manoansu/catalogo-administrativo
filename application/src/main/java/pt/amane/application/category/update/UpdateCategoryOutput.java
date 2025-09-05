package pt.amane.application.category.update;

import pt.amane.domain.category.Category;

public record UpdateCategoryOutput(
    String id
) {

  //Factory Method
  public static UpdateCategoryOutput from(final String anId) {
    return new UpdateCategoryOutput(anId);
  }

  public static UpdateCategoryOutput from(final Category aCategory) {
    return new UpdateCategoryOutput(aCategory.getId().getValue());
  }

}
