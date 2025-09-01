package pt.amane.application.video.create;

import pt.amane.UseCase;

public sealed abstract class CreateVideoUseCase
    extends UseCase<CreateVideoCommand, CreateVideoOutput>
    permits CreateVideoUseCaseImpl {

}
