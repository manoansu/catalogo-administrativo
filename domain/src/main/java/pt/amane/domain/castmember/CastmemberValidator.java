package pt.amane.domain.castmember;

import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.Validator;

public class CastmemberValidator extends Validator {

  private static final int NAME_MAX_LENGTH = 255;
  private static final int NAME_MIN_LENGTH = 3;

  private final Castmember castmember;

  protected CastmemberValidator(
      final Castmember castmember,
      final ValidationHandler handler
  ) {
    super(handler);
    this.castmember = (Castmember) ObjectsValidator.objectValidation(castmember);
  }

  @Override
  public void validate() {
    checkNameConstraints();
    checkTypeConstraints();
  }

  private void checkNameConstraints() {
    final var name = this.castmember.getName();
    if (name == null) {
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
  }
    if (name.isBlank()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
      return;
    }

    if (name.length() > NAME_MAX_LENGTH || name.length() < NAME_MIN_LENGTH) {
      this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
    }

    }

  private void checkTypeConstraints() {
    final var type = this.castmember.getType();
    if (type == null) {
      this.validationHandler().append(new Error("'type' should not be null"));
    }
  }

}
