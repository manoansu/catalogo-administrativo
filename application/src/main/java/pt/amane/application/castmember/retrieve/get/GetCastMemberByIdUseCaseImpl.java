package pt.amane.application.castmember.retrieve.get;

import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.validation.ObjectsValidator;

public non-sealed class GetCastMemberByIdUseCaseImpl extends GetCastMemberByIdUseCase {

  private final CastMemberGateway castMemberGateway;

  public GetCastMemberByIdUseCaseImpl(CastMemberGateway castMemberGateway) {
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
  }

  @Override
  public GetCastMemberByIdOutput execute(String anIn) {
    final var aMember = CastMemberID.from(anIn);
    return this.castMemberGateway.findById(aMember)
        .map(GetCastMemberByIdOutput::from)
        .orElseThrow(() -> NotFoundException.with(CastMember.class, aMember));
  }
}
