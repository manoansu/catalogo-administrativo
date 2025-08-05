package pt.amane.application.genre.update;

import java.time.Instant;
import java.util.List;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.Genre;

public record UpdateGenreOutput(
    String id,
    String name,
    boolean isActive,
    List<String> categories,
    Instant createdAt,
    Instant updatedAt,
    Instant deletedAt
) {

  public static UpdateGenreOutput from(final Genre agenre) {
    return new UpdateGenreOutput(
        agenre.getId().getValue(),
        agenre.getName(),
        agenre.isActive(),
        agenre.getCategories().stream()
            .map(CategoryID::getValue).toList(),
        agenre.getCreatedAt(),
        agenre.getUpdatedAt(),
        agenre.getDeletedAt()
    );
  }
}
