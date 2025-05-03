package pt.amane.domain.validation.handler;

import java.util.List;
import pt.amane.domain.exception.DomainException;
import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ValidationHandler;

public class ThrowsValidationHandler implements ValidationHandler {

  @Override
  public ValidationHandler append(final Error anError) {
    throw DomainException.with(anError);
  }

  @Override
  public ValidationHandler append(ValidationHandler anHandler) {
    throw DomainException.with(anHandler.getErrors());
  }

  @Override
  public <T> T validate(Validation<T> aValidation) {
    try {
      return aValidation.validate();
    }catch (final Exception e) {
      throw DomainException.with(new Error(e.getMessage()));
    }
  }

  @Override
  public List<Error> getErrors() {
    return List.of();
  }
}
