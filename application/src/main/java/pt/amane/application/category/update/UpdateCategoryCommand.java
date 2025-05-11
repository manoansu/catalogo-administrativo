package pt.amane.application.category.update;

import pt.amane.domain.category.Category;

public record UpdateCategoryCommand(
    String id,
    String name,
    String description,
    boolean isActive
) {

  //Factory method
  public static UpdateCategoryCommand with(
      final String anId,
      final String aName,
      final String aDescription,
      final boolean isActive

  ){
    return new UpdateCategoryCommand(anId, aName, aDescription, isActive);
  }

  public static UpdateCategoryCommand form(final Category aCategory) {
    return new UpdateCategoryCommand(
        aCategory.getId().getValue(),
        aCategory.getName(),
        aCategory.getDescription(),
        aCategory.isActive()
    );
  }
}
