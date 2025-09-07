package pt.amane.infrastructure.configuration.usecase;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.category.create.CreateCategoryUseCase;
import pt.amane.application.category.create.CreateCategoryUseCaseImpl;
import pt.amane.application.category.delete.DeleteCategoryUseCase;
import pt.amane.application.category.delete.DeleteCategoryUseCaseImpl;
import pt.amane.application.category.retrieve.get.GetCategoryByIdUseCase;
import pt.amane.application.category.retrieve.get.GetCategoryByIdUseCaseImpl;
import pt.amane.application.category.retrieve.list.ListCategoriesUseCase;
import pt.amane.application.category.retrieve.list.ListCategoriesUseCaseImpl;
import pt.amane.application.category.update.UpdateCategoryUseCase;
import pt.amane.application.category.update.UpdateCategoryUseCaseImpl;
import pt.amane.domain.category.CategoryGateway;

@Configuration
public class CategoryUseCaseConfig {

  private final CategoryGateway categoryGateway;

  public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
  }

  @Bean
  public CreateCategoryUseCase createCategoryUseCase() {
    return new CreateCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public UpdateCategoryUseCase updateCategoryUseCase() {
    return new UpdateCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public GetCategoryByIdUseCase getCategoryByIdUseCase() {
    return new GetCategoryByIdUseCaseImpl(categoryGateway);
  }

  @Bean
  public ListCategoriesUseCase listCategoriesUseCase() {
    return new ListCategoriesUseCaseImpl(categoryGateway);
  }

  @Bean
  public DeleteCategoryUseCase deleteCategoryUseCase() {
    return new DeleteCategoryUseCaseImpl(categoryGateway);
  }
}