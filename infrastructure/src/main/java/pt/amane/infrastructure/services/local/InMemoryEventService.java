package pt.amane.infrastructure.services.local;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pt.amane.infrastructure.configuration.json.Json;
import pt.amane.infrastructure.services.EventService;

public class InMemoryEventService implements EventService {

  private static final Logger LOGGER = LoggerFactory.getLogger(InMemoryEventService.class);

  @Override
  public void send(Object event) {
    LOGGER.info("Event was observed: {}", Json.writeValueAsString(event));
  }
}