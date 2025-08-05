package pt.amane.application.castmember.delete;

import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.validation.ObjectsValidator;

public non-sealed class DeleteCastMemberUseCaseImpl extends DeleteCastMemberUseCase{

  private final CastMemberGateway castMemberGateway;

  public DeleteCastMemberUseCaseImpl(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
  }

  @Override
  public void execute(String anIn) {
   this.castMemberGateway.deleteById(CastMemberID.from(anIn));
  }
}
