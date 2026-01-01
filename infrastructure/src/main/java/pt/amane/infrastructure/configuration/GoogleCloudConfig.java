package pt.amane.infrastructure.configuration;

import com.google.api.gax.retrying.RetrySettings;
import com.google.auth.Credentials;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.NoCredentials;
import com.google.cloud.http.HttpTransportOptions;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.threeten.bp.Duration;
import pt.amane.infrastructure.configuration.proprieties.google.GoogleCloudProperties;
import pt.amane.infrastructure.configuration.proprieties.google.GoogleStorageProperties;

@Configuration
//@Profile({"development", "production"}) // inform spring that I want read my configuration on development and production.
@Profile({"!development & !test-integration & !test-e2e"}) // inform spring that I want read my configuration different from development and tests.
public class GoogleCloudConfig {

  @Bean
  @ConfigurationProperties("google.cloud")
  public GoogleCloudProperties googleCloudProperties() {
    return new GoogleCloudProperties();
  }

  @Bean
  @ConfigurationProperties("google.cloud.storage.catalogo-videos")
  public GoogleStorageProperties googleStorageProperties() {
    return new GoogleStorageProperties();
  }

  @Bean
  public Credentials credentials(final GoogleCloudProperties props) throws IOException {
    if (props.getCredentials() != null) {
      final var jsonBin = Base64.getDecoder().decode(props.getCredentials());
      return GoogleCredentials.fromStream(new ByteArrayInputStream(jsonBin));
    }

    try {
      return GoogleCredentials.getApplicationDefault();
    } catch (Exception e) {
      return NoCredentials.getInstance();
    }
  }

  @Bean
  public Storage storage(
      final Credentials credentials,
      final GoogleCloudProperties cloudConfig,
      final GoogleStorageProperties storageConfig
  ) {
    final var transportOptions = HttpTransportOptions.newBuilder()
        .setConnectTimeout(storageConfig.getConnectTimeout())
        .setReadTimeout(storageConfig.getReadTimeout())
        .build();

    final var retry = RetrySettings.newBuilder()
        .setInitialRetryDelay(Duration.ofMillis(storageConfig.getRetryDelay()))
        .setMaxRetryDelay(Duration.ofMillis(storageConfig.getRetryMaxDelay()))
        .setMaxAttempts(storageConfig.getRetryMaxAttempts())
        .setRetryDelayMultiplier(storageConfig.getRetryMultiplier() > 0 ? storageConfig.getRetryMultiplier() : 1.0)
        .build();

    return StorageOptions.newBuilder()
        .setCredentials(credentials)
        .setProjectId(cloudConfig.getProjectId())
        .setTransportOptions(transportOptions)
        .setRetrySettings(retry)
        .build()
        .getService();
  }
}
