package pt.amane;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.validation.ObjectsValidator;
import pt.amane.infrastructure.castmember.CastMemberGatewayImpl;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;
import pt.amane.infrastructure.category.CategoryGatewayImpl;
import pt.amane.infrastructure.category.persistence.CategoryRepository;
import pt.amane.infrastructure.genre.GenreGatewayImpl;
import pt.amane.infrastructure.genre.persistence.GenreRepository;

@TestConfiguration
public class TestConfig {

  private final CategoryRepository categoryRepository;
  private final CastMemberRepository castMemberRepository;
  private final GenreRepository genreRepository;

  public TestConfig(
      final CategoryRepository categoryRepository,
      final CastMemberRepository castMemberRepository,
      final GenreRepository genreRepository) {
    this.categoryRepository = (CategoryRepository) ObjectsValidator.objectValidation(categoryRepository);
    this.castMemberRepository = (CastMemberRepository) ObjectsValidator.objectValidation(castMemberRepository);
    this.genreRepository = (GenreRepository) ObjectsValidator.objectValidation(genreRepository);
  }

  @Bean
  public CategoryGateway categoryGateway() {
    return new CategoryGatewayImpl(categoryRepository);
  }

  @Bean
  public CastMemberGateway castMemberGateway() {
    return new CastMemberGatewayImpl(castMemberRepository);
  }

  @Bean
  public GenreGateway genreGateway() {
    return new GenreGatewayImpl(genreRepository);
  }

}
