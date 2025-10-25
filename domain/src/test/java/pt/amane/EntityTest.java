package pt.amane;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.amane.domain.event.DomainEvent;
import pt.amane.domain.utils.IdUtils;
import pt.amane.domain.validation.ValidationHandler;

class EntityTest {

  @Test
  void givenNullAsEvents_whenInstantiate_shouldBeOk() {

    //given
    final List<DomainEvent> events = null;

    //when
    final var anEntity = new DummyEntity(new DummyID(), events);

    //then
    Assertions.assertNotNull(anEntity.getDomainEvents());
    Assertions.assertTrue(anEntity.getDomainEvents().isEmpty());
  }

  @Test
  void givenDomainEvents_whenPassInConstructor_shouldCreateADefensiveClone() {

    //given
    final List<DomainEvent> events = new ArrayList<>();
    events.add(new DummyEvent());

    //when
    final var anEntity = new DummyEntity(new DummyID(), events);

    //then
    Assertions.assertNotNull(anEntity.getDomainEvents());
    Assertions.assertEquals(anEntity.getDomainEvents().size(), 1);
  }

  @Test
  void givenEmptyDomainEvents_whenCallsRegisterEvent_shouldAddEventToList() {

    // given
    final var expectedEvents = 1;
    final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

    // when
    anEntity.registerDomainEvent(new DummyEvent());

    // then
    Assertions.assertNotNull(anEntity.getDomainEvents());
    Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());

  }

  @Test
  void givenAFewDomainEvents_whenCallsPublishEvents_shouldCallPublisherAndClearTheList() {

    // given
    final var expectedEvents = 0;
    final var expectedSentEvents = 2;
    final var counter = new AtomicInteger(0);
    final var anEntity = new DummyEntity(new DummyID(), new ArrayList<>());

    anEntity.registerDomainEvent(new DummyEvent());
    anEntity.registerDomainEvent(new DummyEvent());

    Assertions.assertEquals(2, anEntity.getDomainEvents().size());

    // when
    anEntity.publishDomainEvents(event -> {
      counter.incrementAndGet();
    });

    // then
    Assertions.assertNotNull(anEntity.getDomainEvents());
    Assertions.assertEquals(expectedEvents, anEntity.getDomainEvents().size());
    Assertions.assertEquals(expectedSentEvents, counter.get());
  }

  public static class DummyEvent implements DomainEvent {

    @Override
    public Instant occurredOn() {
      return null;
    }
  }

  public static class DummyID extends Identifier {

    private final String id;

    public DummyID() {
      this.id = IdUtils.uuid();
    }

    @Override
    public String getValue() {
      return this.id;
    }
  }

  public static class  DummyEntity extends Entity<DummyID> {

    protected DummyEntity(final DummyID dummyID, final List<DomainEvent> domainEvents) {
      super(dummyID, domainEvents);
    }

    @Override
    public void validate(final ValidationHandler handler) {

    }
  }
}