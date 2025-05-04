package pt.amane;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import pt.amane.domain.event.DomainEvent;
import pt.amane.domain.event.DomainEventPublisher;
import pt.amane.domain.validation.ValidationHandler;

public abstract class Entity<ID extends Identifier> {

  protected final ID id;

  private final List<DomainEvent> domainEvents;

  protected Entity(final ID id) {
    this(id, null);
  }

  protected Entity(ID id, List<DomainEvent> domainEvents) {
    Objects.requireNonNull(id, "'id' should not be null");
    this.id = id;
    this.domainEvents = new ArrayList<>(domainEvents == null ? Collections.emptyList() : domainEvents);
  }

  public abstract void validate(ValidationHandler handler);

  public ID getId() {
    return id;
  }

  public List<DomainEvent> getDomainEvents() {
    return Collections.unmodifiableList(domainEvents);
  }

  public void publishDomainEvents(final DomainEventPublisher publisher) {
    if (publisher == null) {
      return;
    }

    getDomainEvents().forEach(publisher::publishEvent);

    this.domainEvents.clear();
  }

  public void registerDomainEvent(final DomainEvent event) {
    if (event == null) {
      return;
    }

    this.domainEvents.add(event);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final Entity<?> entity = (Entity<?>) o;
    return Objects.equals(id, entity.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
