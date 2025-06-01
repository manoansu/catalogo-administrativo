package pt.amane.application.gnre.retrieve.get;

import java.time.Instant;
import java.util.List;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.Genre;

public record GenreOutput(
    String id,
    String name,
    boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {

  public static GenreOutput from(final Genre aGenre) {
    return new GenreOutput(
        aGenre.getId().getValue(),
        aGenre.getName(),
        aGenre.isActive(),
        aGenre.getCategories().stream()
            .map(CategoryID::getValue)
            .toList(),
        aGenre.getCreatedAt(),
        aGenre.getUpdatedAt(),
        aGenre.getDeletedAt()
    );
  }
}
