package pt.amane;

import org.springframework.amqp.rabbit.core.RabbitOperations;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import pt.amane.infrastructure.configuration.annotations.VideoCreatedQueue;
import pt.amane.infrastructure.configuration.proprieties.amqp.QueueProperties;
import pt.amane.infrastructure.services.EventService;
import pt.amane.infrastructure.services.impl.RabbitEventService;
import pt.amane.infrastructure.services.local.InMemoryEventService;

@Configuration
public class EventConfig {

  @Bean
  @VideoCreatedQueue
  @Profile({"development"})
  EventService localVideoCreatedEventService() {
    return new InMemoryEventService();
  }

  @Bean
  @VideoCreatedQueue
  @ConditionalOnMissingBean
  EventService videoCreatedEventService(
      @VideoCreatedQueue final QueueProperties props,
      final RabbitOperations ops
  ) {
    return new RabbitEventService(props.getExchange(), props.getRoutingKey(), ops);
  }
}