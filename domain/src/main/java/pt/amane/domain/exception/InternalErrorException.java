package pt.amane.domain.exception;

public class InternalErrorException extends NoStackTraceException {

  public InternalErrorException(final String message, final Throwable cause) {
    super(message, cause);
  }

  public static InternalErrorException with(final String aMessage, final Throwable aCause) {
    return new InternalErrorException(aMessage, aCause);
  }
}
