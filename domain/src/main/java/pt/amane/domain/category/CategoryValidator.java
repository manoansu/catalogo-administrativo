package pt.amane.domain.category;

import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.Validator;

public class CategoryValidator extends Validator {

  private final Category category;

  public CategoryValidator(final Category category, final ValidationHandler handler) {
    super(handler);
    this.category = category;
  }

  @Override
  public void validate() {
    checkNameConstraint();
  }

  private void checkNameConstraint() {
    final var name = this.category.getName();

    if (name == null) {
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

     if (name.isBlank()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
      return;
    }

    final var length = name.trim().length();
    if (length > 255 || length < 3) {
      this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
    }
  }
}
