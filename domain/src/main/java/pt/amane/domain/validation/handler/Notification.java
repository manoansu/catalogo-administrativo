package pt.amane.domain.validation.handler;

import java.util.ArrayList;
import java.util.List;
import pt.amane.domain.exception.DomainException;
import pt.amane.domain.validation.Error;
import pt.amane.domain.validation.ValidationHandler;

public class Notification implements ValidationHandler {

  private final List<Error> errors;

  private Notification(List<Error> errors) {
    this.errors = errors;
  }

  public static Notification create() {
    return new Notification(new ArrayList<>());
  }

  public static Notification create(final Throwable throwable) {
    return create(new Error(throwable.getMessage()));
  }

  public static Notification create(final Error anError) {
    return new Notification(new ArrayList<>()).append(anError);
  }

  @Override
  public Notification append(Error anError) {
    this.errors.add(anError);
    return this;
  }

  @Override
  public ValidationHandler append(ValidationHandler anHandler) {
    this.errors.addAll(anHandler.getErrors());
    return this;
  }

  @Override
  public <T> T validate(Validation<T> aValidation) {
    try {
      return aValidation.validate();
    }catch (final DomainException ex) {
      this.errors.addAll(ex.getErrors());
    }catch (final Throwable t) {
      this.errors.add(new Error(t.getMessage()));
    }
    return null;
  }

  @Override
  public List<Error> getErrors() {
    return this.errors;
  }

}
