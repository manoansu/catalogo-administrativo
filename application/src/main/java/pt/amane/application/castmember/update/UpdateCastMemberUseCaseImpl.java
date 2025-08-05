package pt.amane.application.castmember.update;

import java.util.function.Supplier;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.validation.handler.Notification;

public non-sealed class UpdateCastMemberUseCaseImpl extends UpdateCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public UpdateCastMemberUseCaseImpl(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
  }

  @Override
  public UpdateCastMemberOutput execute(UpdateCastMemberCommand  castMemberCommand) {
    final var anId = CastMemberID.from(castMemberCommand.id());
    final var aName = castMemberCommand.name();
    final var aType = castMemberCommand.type();

    final var aCastMember = castMemberGateway.findById(anId)
        .orElseThrow(notFound(anId));

    final var notification = Notification.create();
    notification.validate(() -> aCastMember.update(aName, aType));

    if (notification.hasErrors()) {
      notify(anId, notification);
    }

    return UpdateCastMemberOutput.from(castMemberGateway.update(aCastMember));
  }

  private void notify(CastMemberID anId, Notification notification) {
    throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId.getValue()), notification);

  }

  private Supplier<NotFoundException> notFound(CastMemberID anId) {
    return () -> NotFoundException.with(CastMember.class, anId);
  }
}
