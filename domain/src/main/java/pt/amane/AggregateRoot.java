package pt.amane;

import java.util.List;
import pt.amane.domain.event.DomainEvent;

public abstract class AggregateRoot<ID extends Identifier> extends Entity<ID> {

  protected AggregateRoot(final ID id) {
    super(id);
  }

  protected AggregateRoot(final ID id, List<DomainEvent> events) {
    super(id, events );
  }

}
