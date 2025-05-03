package pt.amane.domain.event;

public interface DomainEventPublisher {

  void publishEvent(DomainEvent event);

}
