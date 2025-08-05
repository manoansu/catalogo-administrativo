package pt.amane.application.genre.update;

import java.util.List;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.Genre;

public record UpdateGenreCommand(
    String id,
    String name,
    boolean isActive,
    List<String> categories

) {

  public static UpdateGenreCommand with(final String anId, final String aName, final Boolean isActive, final List<String> categories){
    return new UpdateGenreCommand(anId, aName, isActive != null ? isActive : true, categories);
  }

  public static UpdateGenreCommand from(final Genre agenre) {
    return new UpdateGenreCommand(
        agenre.getId().getValue(),
        agenre.getName(),
        agenre.isActive(),
        agenre.getCategories().stream()
            .map(CategoryID::getValue).toList()
    );
  }
}
