package pt.amane.infrastructure.configuration;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import pt.amane.infrastructure.castmember.CastMemberGatewayImpl;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;
import pt.amane.infrastructure.category.CategoryGatewayImpl;
import pt.amane.infrastructure.category.persistence.CategoryRepository;
import pt.amane.infrastructure.genre.GenreGatewayImpl;
import pt.amane.infrastructure.genre.persistence.GenreRepository;
import pt.amane.infrastructure.services.EventService;
import pt.amane.infrastructure.video.VideoGatewayImpl;
import pt.amane.infrastructure.video.persistence.VideoRepository;

@Configuration
public class GatewayConfig {

  public final CategoryRepository categoryRepository;
  public final CastMemberRepository castMemberRepository;
  public final GenreRepository genreRepository;
  public final VideoRepository videoRepository;
  public final EventService eventService;

  public GatewayConfig(
      final CategoryRepository categoryRepository,
      final CastMemberRepository castMemberRepository,
      final GenreRepository genreRepository,
      final VideoRepository videoRepository,
      @Lazy final EventService eventService
  ) {
    this.categoryRepository = Objects.requireNonNull(categoryRepository);
    this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    this.genreRepository = Objects.requireNonNull(genreRepository);
    this.videoRepository = Objects.requireNonNull(videoRepository);
    this.eventService = Objects.requireNonNull(eventService);
  }

  @Bean
  public CategoryGatewayImpl categoryGatewayImpl() {
    return new CategoryGatewayImpl(categoryRepository);
  }

  @Bean
  public CastMemberGatewayImpl castMemberGatewayImpl() {
    return new CastMemberGatewayImpl(castMemberRepository);
  }

  @Bean
  public GenreGatewayImpl genreGatewayImpl() {
    return new GenreGatewayImpl(genreRepository);
  }

  @Bean
  public VideoGatewayImpl videoGatewayImpl() {
    return new VideoGatewayImpl(videoRepository, eventService);
  }
}
