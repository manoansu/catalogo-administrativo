package pt.amane.infrastructure.configuration.proprieties.amqp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;

public class QueueProperties implements InitializingBean {

  private static final Logger logger = LoggerFactory.getLogger(QueueProperties.class);

  private String exchange; // send message to queue

  private String routingKey;

  private String queue;

public QueueProperties(){
}

  @Override
  public String toString() {
    return "QueueProperties{" +
        "exchanges='" + exchange + '\'' +
        ", routingKey='" + routingKey + '\'' +
        ", queue='" + queue + '\'' +
        '}';
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    logger.debug(toString());
  }

  public String getExchange() {
    return exchange;
  }

  public void setExchange(String exchanges) {
    this.exchange = exchanges;
  }

  public String getRoutingKey() {
    return routingKey;
  }

  public void setRoutingKey(String routingKey) {
    this.routingKey = routingKey;
  }

  public String getQueue() {
    return queue;
  }

  public void setQueue(String queue) {
    this.queue = queue;
  }
}
