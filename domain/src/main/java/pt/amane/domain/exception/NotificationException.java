package pt.amane.domain.exception;

import pt.amane.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

  public NotificationException(final String aMessage, final Notification notification) {
    super(aMessage, notification.getErrors());
  }
}
