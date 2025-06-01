package pt.amane.application.gnre.retrieve.list;

import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.domain.validation.ObjectsValidator;

public non-sealed class ListGenreUseCaseImpl extends ListGenreUseCase{

  private final GenreGateway genreGateway;

  public ListGenreUseCaseImpl(GenreGateway genreGateway) {
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
  }

  @Override
  public Pagination<GenreListOutput> execute(SearchQuery aQuery) {
    return this.genreGateway.findAll(aQuery)
        .map(GenreListOutput::from);
  }
}
