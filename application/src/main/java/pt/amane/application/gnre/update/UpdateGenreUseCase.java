package pt.amane.application.gnre.update;

import pt.amane.UseCase;

public sealed abstract class UpdateGenreUseCase
    extends UseCase<UpdateGenreCommand, UpdateGenreOutput>
    permits UpdateGenreUseCaseImpl {

}
