package pt.amane.infrastructure.configuration;

import com.google.cloud.storage.Storage;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile({"test-integration", "e2e"})
public class GoogleCloudConfig {

    @Bean
    @Primary
    public Storage storage() {
        return Mockito.mock(Storage.class);
    }
}