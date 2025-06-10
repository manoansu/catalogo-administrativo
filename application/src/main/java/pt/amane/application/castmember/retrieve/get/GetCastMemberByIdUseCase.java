package pt.amane.application.castmember.retrieve.get;

import pt.amane.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
    extends UseCase<String, GetCastMemberByIdOutput>
    permits GetCastMemberByIdUseCaseImpl {

}
