package pt.amane.application.gnre.retrieve.list;

import java.time.Instant;
import java.util.List;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.Genre;

public record GenreListOutput(
    String id,
    String name,
    boolean isActive,
    List<String> categories,
    Instant created,
    Instant deleted
) {

  public static GenreListOutput from(final Genre aGenre) {
    return new GenreListOutput(
        aGenre.getId().getValue(),
        aGenre.getName(),
        aGenre.isActive(),
        aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
        aGenre.getCreatedAt(),
        aGenre.getDeletedAt()
    );
  }
}
