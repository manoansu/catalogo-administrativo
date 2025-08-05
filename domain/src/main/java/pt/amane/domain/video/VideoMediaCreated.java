package pt.amane.domain.video;

import java.time.Instant;
import pt.amane.domain.event.DomainEvent;
import pt.amane.domain.utils.InstantUtils;

public record VideoMediaCreated(
    String resourceId,
    String filePath,
    Instant occurredOn
) implements DomainEvent {

  public VideoMediaCreated(final String resourceId, final String filePath) {
    this(resourceId, filePath, InstantUtils.now());
  }
}
