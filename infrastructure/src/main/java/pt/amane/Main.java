package pt.amane;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import pt.amane.infrastructure.configuration.WebServerConfig;

@SpringBootApplication(scanBasePackages = "pt.amane")
public class Main {

  public static void main(String[] args) {
    System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "development");
    SpringApplication.run(WebServerConfig.class, args);
  }

//  @Bean
//  public ApplicationRunner runner(CategoryRepository repository) {
//    return args -> {
//      List<CategoryJpaEntity> all = repository.findAll();
//
//      Category category = Category.newCategory("Filmes", "Novo Filme", true);
//
//      repository.saveAndFlush(CategoryJpaEntity.from(category));
//
//      repository.deleteAll();
//    };
//  }

  // Testar o bean de cada caso de uso
//  @Bean
//  @DependsOnDatabaseInitialization
//  ApplicationRunner runner(
//      @Autowired CreateCategoryUseCase createCategoryUseCase,
//      @Autowired UpdateCategoryUseCase updateCategoryUseCase,
//      @Autowired DeleteCategoryUseCase deleteCategoryUseCase,
//      @Autowired ListCategoriesUseCase listCategoriesUseCase,
//      @Autowired GetCategoryByIdUseCase getCategoryByIdUseCase
//  ) {
//    return args -> {
//
//    };
//  }
}