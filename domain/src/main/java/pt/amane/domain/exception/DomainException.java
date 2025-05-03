package pt.amane.domain.exception;

import java.util.List;
import pt.amane.domain.validation.Error;

public class DomainException extends NoStackTraceException {

  private List<Error> errors;

  /**
   * Allow domain exception to save errors.
   * @param aMessage
   * @param anErrors
   */
  protected DomainException(final String aMessage, List<Error> anErrors) {
    super(aMessage);
    this.errors = anErrors;
  }

  /**
   * Fatore method that allow Error as patameter..
   * @param anError
   * @return
   */
  public static DomainException with(final Error anError) {
    return new DomainException(anError.message(), List.of(anError));
  }

  /**
   * Fatore method that allow list of Error as patameter..
   * @param anError
   * @return
   */
  public static DomainException with(final List<Error> anError) {
    return new DomainException("", anError);
  }

  public List<Error> getErrors() {
    return errors;
  }
}
