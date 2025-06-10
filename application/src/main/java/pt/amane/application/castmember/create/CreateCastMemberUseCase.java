package pt.amane.application.castmember.create;

import pt.amane.UseCase;

public sealed abstract class CreateCastMemberUseCase
    extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
    permits CreateCastMemberUseCaseImpl  {

}
