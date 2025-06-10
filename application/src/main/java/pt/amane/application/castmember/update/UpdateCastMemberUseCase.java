package pt.amane.application.castmember.update;

import pt.amane.UseCase;

public sealed abstract class UpdateCastMemberUseCase
    extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
    permits UpdateCastMemberUseCaseImpl {

}
