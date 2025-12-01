package pt.amane.application.castmember.create;

import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.domain.validation.handler.Notification;

public class CreateCastMemberUseCaseImpl extends CreateCastMemberUseCase {

  private final CastMemberGateway castMemberGateway;

  public CreateCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
  }

  @Override
  public CreateCastMemberOutput execute(CreateCastMemberCommand castMemberCommand) {
    final var aName = castMemberCommand.name();
    final var aType = castMemberCommand.type();

    final var notification = Notification.create();

    final var aMember = notification.validate(() -> CastMember.newCastmember(aName, aType));

    if (notification.hasErrors()) {
      notify(notification);
    }

    return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
  }

  private void notify(Notification notification) {
    throw new NotificationException("Could not create Aggregate CastMember", notification);
  }
}
