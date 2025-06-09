package pt.amane.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.gnre.create.CreateGenreUseCaseImpl;
import pt.amane.application.gnre.delete.DeleteGenreUseCaseImpl;
import pt.amane.application.gnre.retrieve.get.GetGenreByIdUseCaseImpl;
import pt.amane.application.gnre.retrieve.list.ListGenreUseCaseImpl;
import pt.amane.application.gnre.update.UpdateGenreUseCaseImpl;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.validation.ObjectsValidator;

@Configuration
public class GenreUseCaseConfig {

  private final CategoryGateway categoryGateway;
  private final GenreGateway genreGateway;

  public GenreUseCaseConfig(CategoryGateway categoryGateway, GenreGateway genreGateway) {
    this.categoryGateway = (CategoryGateway) ObjectsValidator.objectValidation(categoryGateway);
    this.genreGateway = (GenreGateway) ObjectsValidator.objectValidation(genreGateway);
  }

  @Bean
  public CreateGenreUseCaseImpl createGenreUseCase() {
    return new CreateGenreUseCaseImpl(categoryGateway, genreGateway);
  }

  @Bean
  public GetGenreByIdUseCaseImpl getGenreByIdUseCase() {
    return new GetGenreByIdUseCaseImpl(genreGateway);
  }


  @Bean
  public UpdateGenreUseCaseImpl updateGenreUseCase() {
    return new UpdateGenreUseCaseImpl(categoryGateway, genreGateway);
  }

  @Bean
  public DeleteGenreUseCaseImpl deleteGenreUseCase() {
    return new DeleteGenreUseCaseImpl(genreGateway);
  }

 @Bean
  public ListGenreUseCaseImpl listGenreUseCase() {
    return new ListGenreUseCaseImpl(genreGateway);
  }
}
