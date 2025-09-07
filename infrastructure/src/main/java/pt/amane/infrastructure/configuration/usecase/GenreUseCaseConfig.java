package pt.amane.infrastructure.configuration.usecase;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.genre.create.CreateGenreUseCase;
import pt.amane.application.genre.create.CreateGenreUseCaseImpl;
import pt.amane.application.genre.delete.DeleteGenreUseCase;
import pt.amane.application.genre.delete.DeleteGenreUseCaseImpl;
import pt.amane.application.genre.retrieve.get.GetGenreByIdUseCase;
import pt.amane.application.genre.retrieve.get.GetGenreByIdUseCaseImpl;
import pt.amane.application.genre.retrieve.list.ListGenreUseCase;
import pt.amane.application.genre.retrieve.list.ListGenreUseCaseImpl;
import pt.amane.application.genre.update.UpdateGenreUseCase;
import pt.amane.application.genre.update.UpdateGenreUseCaseImpl;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.genre.GenreGateway;

@Configuration
public class GenreUseCaseConfig {

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public GenreUseCaseConfig(
      final CategoryGateway categoryGateway,
      final GenreGateway genreGateway
  ) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    this.genreGateway = Objects.requireNonNull(genreGateway);
  }

  @Bean
  public CreateGenreUseCase createGenreUseCase() {
    return new CreateGenreUseCaseImpl(categoryGateway, genreGateway);
  }

  @Bean
  public DeleteGenreUseCase deleteGenreUseCase() {
    return new DeleteGenreUseCaseImpl(genreGateway);
  }

  @Bean
  public GetGenreByIdUseCase getGenreByIdUseCase() {
    return new GetGenreByIdUseCaseImpl(genreGateway);
  }

  @Bean
  public ListGenreUseCase listGenreUseCase() {
    return new ListGenreUseCaseImpl(genreGateway);
  }

  @Bean
  public UpdateGenreUseCase updateGenreUseCase() {
    return new UpdateGenreUseCaseImpl(categoryGateway, genreGateway);
  }
}