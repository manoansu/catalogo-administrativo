package pt.amane.application.category.update;

import pt.amane.domain.category.Category;

public record UpdateCategoryOutput(
    String id
) {

  //Factory Method
  public static UpdateCategoryOutput with(final String anId) {
    return new UpdateCategoryOutput(anId);
  }

  public static UpdateCategoryOutput form(final Category aCategory) {
    return new UpdateCategoryOutput(aCategory.getId().getValue());
  }

}
