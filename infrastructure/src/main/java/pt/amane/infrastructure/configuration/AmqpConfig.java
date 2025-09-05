package pt.amane.infrastructure.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.infrastructure.configuration.annotations.VideoCreatedQueue;
import pt.amane.infrastructure.configuration.annotations.VideoEncodedQueue;
import pt.amane.infrastructure.configuration.annotations.VideoEvents;
import pt.amane.infrastructure.configuration.proprieties.amqp.QueueProperties;

@Configuration
public class AmqpConfig {

  /**
   * // Using good practice its important to create this configuration,
   * instead to add on main class, because the main class can has two queue and you will need to create twoa class.
   * whit this annotations you can only has one queue. or create two configuration different.
   * @return
   */
  @Bean
  @ConfigurationProperties("amqp.queues.video-created")
  @VideoCreatedQueue
  QueueProperties videoCreatedQueueProperties() {
    return new QueueProperties();
  }

  @Bean
  @ConfigurationProperties("amqp.queues.video-encoded")
  @VideoEncodedQueue
  QueueProperties videoEncodedQueueProperties() {
    return new QueueProperties();
  }

  @Configuration
  static class Admin {

    @Bean
    @VideoEvents
    Exchange videoEventsExchange(@VideoCreatedQueue QueueProperties props) {
      return new DirectExchange(props.getExchange());
    }

    @Bean
    @VideoCreatedQueue
    Queue videoCreatedQueue(@VideoCreatedQueue QueueProperties props) {
      return new Queue(props.getQueue());
    }

    @Bean
    @VideoCreatedQueue
    Binding videoCreatedQueueBinding(
        @VideoEvents DirectExchange exchange,
        @VideoCreatedQueue Queue queue,
        @VideoCreatedQueue QueueProperties props
    ) {
      return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
    }

    @Bean
    @VideoEncodedQueue
    Queue videoEncodedQueue(@VideoEncodedQueue QueueProperties props) {
      return new Queue(props.getQueue());
    }

    @Bean
    @VideoEncodedQueue
    Binding videoEncodedQueueBinding(
        @VideoEvents DirectExchange exchange,
        @VideoEncodedQueue Queue queue,
        @VideoEncodedQueue QueueProperties props
    ) {
      return BindingBuilder.bind(queue).to(exchange).with(props.getRoutingKey());
    }
  }
}