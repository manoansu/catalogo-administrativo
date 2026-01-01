package pt.amane;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import pt.amane.infrastructure.configuration.WebServerConfig;

@SpringBootApplication(scanBasePackages = "pt.amane")
public class Main {

  private static final Logger LOG = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) {
    LOG.info("[step:to-be-init] [id:1] Inicializando o Spring");
    System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "sandbox");

    if (System.getenv("AMQP_RABBIT_HOST") == null) {
      System.setProperty("AMQP_RABBIT_HOST", "127.0.0.1");
      System.setProperty("AMQP_RABBIT_PORT", "5672");
      System.setProperty("AMQP_RABBIT_USERNAME", "guest");
      System.setProperty("AMQP_RABBIT_PASSWORD", "guest");
      System.setProperty("spring.rabbitmq.listener.simple.auto-startup", "false");
    }

    SpringApplication.run(WebServerConfig.class, args);
    LOG.info("[step:inittialized] [id:2] Spring inicializado..");
  }

//  @RabbitListener(queues = "video.encoded.queue")
//  void dummyListener() {
//  }

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