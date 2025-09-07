package pt.amane.infrastructure.configuration.usecase;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.category.create.CreateCategoryUseCase;
import pt.amane.application.category.delete.DeleteCategoryUseCase;
import pt.amane.application.category.retrieve.get.GetCategoryByIdUseCase;
import pt.amane.application.category.retrieve.list.ListCategoriesUseCase;
import pt.amane.application.category.update.UpdateCategoryUseCase;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.infrastructure.api.controllers.CategoryController;

@Configuration
public class CategoryControllerConfig {

  private final CreateCategoryUseCase createCategoryUseCase;
  private final GetCategoryByIdUseCase getCategoryByIdUseCase;
  private final UpdateCategoryUseCase updateCategoryUseCase;
  private final DeleteCategoryUseCase deleteCategoryUseCase;
  private final ListCategoriesUseCase listCategoriesUseCase;

  public CategoryControllerConfig(
      final CreateCategoryUseCase createCategoryUseCase,
      final GetCategoryByIdUseCase getCategoryByIdUseCase,
      final UpdateCategoryUseCase updateCategoryUseCase,
      final DeleteCategoryUseCase deleteCategoryUseCase,
      final ListCategoriesUseCase listCategoriesUseCase
  ) {
    this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
    this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
    this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
    this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
    this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
  }

  @Bean
  public CategoryController categoryController() {
    return new CategoryController(
        createCategoryUseCase,
        getCategoryByIdUseCase,
        updateCategoryUseCase,
        deleteCategoryUseCase,
        listCategoriesUseCase
    );
  }
}