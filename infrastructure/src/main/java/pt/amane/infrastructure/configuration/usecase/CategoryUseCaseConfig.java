package pt.amane.infrastructure.configuration.usecase;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.category.create.CreateCategoryUseCaseImpl;
import pt.amane.application.category.delete.DeleteCategoryUseCaseImpl;
import pt.amane.application.category.retrieve.get.GetCategoryByIdUseCaseImpl;
import pt.amane.application.category.retrieve.list.ListCategoriesUseCaseImpl;
import pt.amane.application.category.update.UpdateCategoryUseCaseImpl;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.validation.ObjectsValidator;

@Configuration
public class CategoryUseCaseConfig {

  private final CategoryGateway categoryGateway;

  public CategoryUseCaseConfig(CategoryGateway categoryGateway) {
    this.categoryGateway = (CategoryGateway) ObjectsValidator.objectValidation(categoryGateway);
  }

  @Bean
  public CreateCategoryUseCaseImpl createCategoryUseCase() {
    return new CreateCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public GetCategoryByIdUseCaseImpl getCategoryByIdUseCase() {
    return new GetCategoryByIdUseCaseImpl(categoryGateway);
  }


  @Bean
  public UpdateCategoryUseCaseImpl updateCategoryUseCase() {
    return new UpdateCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public DeleteCategoryUseCaseImpl deleteCategoryUseCase() {
    return new DeleteCategoryUseCaseImpl(categoryGateway);
  }

  @Bean
  public ListCategoriesUseCaseImpl listCategoryUseCase() {
    return new ListCategoriesUseCaseImpl(categoryGateway);
  }
}
