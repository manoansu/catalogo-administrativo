package pt.amane.application.gnre.delete;

import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.validation.ObjectsValidator;

public non-sealed class DeleteGenreUseCaseImpl extends DeleteGenreUseCase{

  private final GenreGateway genreGateway;

  public DeleteGenreUseCaseImpl(GenreGateway genreGateway) {
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
  }

  @Override
  public void execute(final String anIn) {
    this.genreGateway.deleteById(GenreID.from(anIn));
  }

}
