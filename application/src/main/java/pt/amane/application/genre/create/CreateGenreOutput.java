package pt.amane.application.genre.create;

import pt.amane.domain.genre.Genre;

public record CreateGenreOutput(
    String id
) {

  public static CreateGenreOutput with(final String anId) {
    return new CreateGenreOutput(anId);
  }

  public static CreateGenreOutput from(final Genre genre) {
    return new CreateGenreOutput(genre.getId().getValue());
  }
}
