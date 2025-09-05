package pt.amane.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
//import pt.amane.infrastructure.configuration.proprieties.google.GoogleStorageProperties;
import pt.amane.infrastructure.configuration.proprieties.storage.StorageProperties;
import pt.amane.infrastructure.services.StorageService;
import pt.amane.infrastructure.services.impl.GCStorageService;
import pt.amane.infrastructure.services.local.InMemoryStorageService;

@Configuration
public class StorageConfig {

  @Bean
  @ConfigurationProperties(value = "storage.catalogo-videos")
  public StorageProperties storageProperties() {
    return new StorageProperties();
  }

  @Bean
  @Profile({"development", "test-integration", "test-e2e"})
  public StorageService localStorageAPI() {
    return new InMemoryStorageService();
  }

//  @Bean
//  @ConditionalOnMissingBean
//  public StorageService gcStorageAPI(
//      final GoogleStorageProperties props,
//      final Storage storage
//  ) {
//    return new GCStorageService(props.getBucket(), storage);
//  }
}