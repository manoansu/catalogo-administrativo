package pt.amane.domain.castmember;

import java.time.Instant;
import pt.amane.AggregateRoot;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.utils.InstantUtils;
import pt.amane.domain.validation.ValidationHandler;
import pt.amane.domain.validation.handler.Notification;

public class Castmember extends AggregateRoot<CastmemberID> {

  private String name;
  private CastMemberType type;
  private Instant createdAt;
  private Instant updatedAt;

  protected Castmember(
      final CastmemberID castmemberID,
      final String name,
      final CastMemberType type,
      final Instant createdAt,
      final Instant updatedAt)
  {
    super(castmemberID);
    this.name = name;
    this.type = type;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    selfValidate();
  }

  public static Castmember newCastmember(final String name, final CastMemberType type) {
    final var now = InstantUtils.now();
    return new Castmember(CastmemberID.unique(), name, type, now, now);
  }

  public static Castmember with(
      final CastmemberID castmemberID,
      final String name,
      final CastMemberType type,
      final Instant createdAt,
      final Instant updatedAt) {
    return new Castmember(castmemberID, name, type, createdAt, updatedAt);

  }

  public static Castmember with(Castmember castmember) {
    return new Castmember(
        castmember.getId(),
        castmember.getName(),
        castmember.getType(),
        castmember.getCreatedAt(),
        castmember.getUpdatedAt());
  }

  public Castmember update(final String name, final CastMemberType type) {
    this.name = name;
    this.type = type;
    this.updatedAt = Instant.now();
    selfValidate();
    return this;
  }

  private void selfValidate() {
    final var notification = Notification.create();
    validate(notification);

    if (notification.hasErrors()) {
      throw new NotificationException("Failed to create a Aggregate CastMember", notification);
    }
  }


  @Override
  public void validate(ValidationHandler handler) {
    new CastmemberValidator(this, handler).validate();
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public CastMemberType getType() {
    return type;
  }

  public void setType(CastMemberType type) {
    this.type = type;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  public Instant getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(Instant updatedAt) {
    this.updatedAt = updatedAt;
  }
}
