package pt.amane.application.genre.create;

import pt.amane.UseCase;

/**
 * In java world, you always work for abstration, not for implmentation,
 * because when this class has a new feature , you can only change the implementation not abstration.
 */
public sealed abstract class CreateGenreUseCase
    extends UseCase<CreateGenreCommand,  CreateGenreOutput>
    permits CreateGenreUseCaseImpl {

}
