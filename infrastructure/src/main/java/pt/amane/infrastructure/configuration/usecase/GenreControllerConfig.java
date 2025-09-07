package pt.amane.infrastructure.configuration.usecase;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.genre.create.CreateGenreUseCase;
import pt.amane.application.genre.delete.DeleteGenreUseCase;
import pt.amane.application.genre.retrieve.get.GetGenreByIdUseCase;
import pt.amane.application.genre.retrieve.list.ListGenreUseCase;
import pt.amane.application.genre.update.UpdateGenreUseCase;
import pt.amane.infrastructure.api.controllers.GenreController;

@Configuration
public class GenreControllerConfig {

  private final CreateGenreUseCase createGenreUseCase;
  private final DeleteGenreUseCase deleteGenreUseCase;
  private final GetGenreByIdUseCase getGenreByIdUseCase;
  private final ListGenreUseCase listGenreUseCase;
  private final UpdateGenreUseCase updateGenreUseCase;

  public GenreControllerConfig(
      final CreateGenreUseCase createGenreUseCase,
      final DeleteGenreUseCase deleteGenreUseCase,
      final GetGenreByIdUseCase getGenreByIdUseCase,
      final ListGenreUseCase listGenreUseCase,
      final UpdateGenreUseCase updateGenreUseCase
  ) {
    this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
    this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
    this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
    this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
    this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
  }


  @Bean
  public GenreController genreController() {
    return new GenreController(
        createGenreUseCase,
        deleteGenreUseCase,
        getGenreByIdUseCase,
        listGenreUseCase,
        updateGenreUseCase
    );
  }
}