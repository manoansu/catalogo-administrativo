package pt.amane.domain.genre;

import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.Validator;

public class GenreValidator extends Validator {

  public static final int NAME_MAX_LENGTH = 255;
  public static final int NAME_MIN_LENGTH = 1;

  private final Genre genre;

  GenreValidator(final Genre genre, final ValidationHandler handler) {
    super(handler);
    this.genre = genre;
  }

  @Override
  public void validate() {
    checkNameConstraints();
  }

  private void checkNameConstraints() {

    final var name = this.genre.getName();
    if (name == null) {
      this.validationHandler().append(new Error("'name' should not be null"));
      return;
    }

    if (name.isBlank()) {
      this.validationHandler().append(new Error("'name' should not be empty"));
      return;
    }

    final int length = name.trim().length();
    if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
      this.validationHandler().append(new Error("'name' must be between 1 and 255 characters"));
    }
  }
}
