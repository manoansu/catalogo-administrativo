package pt.amane.application.genre.update;

import pt.amane.UseCase;

public sealed abstract class UpdateGenreUseCase
    extends UseCase<UpdateGenreCommand, UpdateGenreOutput>
    permits UpdateGenreUseCaseImpl {

}
