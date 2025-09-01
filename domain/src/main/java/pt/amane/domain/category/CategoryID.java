package pt.amane.domain.category;

import java.util.Objects;
import pt.amane.Identifier;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ObjectsValidator;

/**
 * Class CategoryID generate Id from classes.
 */
public class CategoryID extends Identifier {

  private final String value;

  public CategoryID(final String categoryID) {
    this.value = ObjectsValidator.objectValidation(categoryID);
  }

  /**
   * Generate the unique ID from category entity class for each category
   * @return
   */
  public static CategoryID unique() {
    return CategoryID.from(IdUtils.uuid());
  }

  /**
   * Factory method that clone the categoryID constructor, either help to convert database value or test.
   * @param anId
   * @return
   */
  public static CategoryID from(final String anId) {
    return new CategoryID(anId);
  }

  @Override
  public String getValue() {
    return this.value;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final CategoryID that = (CategoryID) o;
    return Objects.equals(getValue(), that.getValue());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(getValue());
  }
}
