package pt.amane.domain.exception;

import java.util.Collections;
import java.util.List;
import pt.amane.AggregateRoot;
import pt.amane.Identifier;
import pt.amane.domain.validation.Error;

public class NotFoundException extends DomainException {

  /**
   * Allow domain exception to save errors.
   *
   * @param aMessage
   * @param anErrors
   */
  protected NotFoundException(String aMessage,
      List<Error> anErrors) {
    super(aMessage, anErrors);
  }

  public static NotFoundException with(final Class<? extends AggregateRoot<?>> anAggregate, final
      Identifier id) {
    final var anError = "%s with ID %s was not found".formatted(anAggregate.getSimpleName(), id.getValue());
    return new NotFoundException(anError, Collections.emptyList());
  }

  public static NotFoundException with(final Error anError) {
    return new NotFoundException(anError.message(), List.of(anError));
  }
}
