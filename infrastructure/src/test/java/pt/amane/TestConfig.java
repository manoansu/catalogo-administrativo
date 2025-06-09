package pt.amane;

import java.util.Objects;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pt.amane.infrastructure.category.CategoryMySQLGateway;
import pt.amane.infrastructure.category.persistence.CategoryRepository;

@TestConfiguration
public class TestConfig {

  private final CategoryRepository categoryRepository;

  public TestConfig(CategoryRepository categoryRepository) {
    this.categoryRepository = Objects.requireNonNull(categoryRepository);
  }

  @Bean
  public CategoryMySQLGateway categoryMySQLGateway() {
    return new CategoryMySQLGateway(categoryRepository);
  }
}
