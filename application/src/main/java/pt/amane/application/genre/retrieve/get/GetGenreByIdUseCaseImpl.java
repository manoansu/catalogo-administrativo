package pt.amane.application.genre.retrieve.get;

import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.validation.ObjectsValidator;

public non-sealed class GetGenreByIdUseCaseImpl extends GetGenreByIdUseCase {

  private  final GenreGateway genreGateway;

  public GetGenreByIdUseCaseImpl(GenreGateway genreGateway) {
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
  }

  @Override
  public GenreOutput execute(String anIn) {
    final var genreId = GenreID.from(anIn);
    return this.genreGateway.findById(genreId)
            .map(GenreOutput::from)
                .orElseThrow(() -> NotFoundException.with(Genre.class, genreId));
  }
}
