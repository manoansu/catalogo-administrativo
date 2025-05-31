package pt.amane.domain.validation;

import java.util.List;

public interface ValidationHandler {

  ValidationHandler append(Error anError);

  ValidationHandler append(ValidationHandler anHandler);

  <T> T validate(Validation<T> aValidation);

  List<Error> getErrors();

  /**
   * verify if list hass error or not.
   * @return
   */
  default boolean hasErrors() {
    return getErrors() != null && !getErrors().isEmpty();
  }

  default Error firstError() {
    if (getErrors() != null && !getErrors().isEmpty()) {
      return getErrors().get(0);
    }else {
      return null;
    }
  }

  /**
   * Possibility of launching a method as a lambda function that throws an exception.
   */
  interface Validation<T> {
    T validate();
  }

}
