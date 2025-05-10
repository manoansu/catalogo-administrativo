package pt.amane.application.category.create;

public record CreateCategoryCommand(
    String name,
    String description,
    boolean isActive
) {

  public static CreateCategoryCommand with(final String aName, String aDescription, final boolean isActive) {
    return new CreateCategoryCommand(aName,aDescription,isActive);
  }
}
