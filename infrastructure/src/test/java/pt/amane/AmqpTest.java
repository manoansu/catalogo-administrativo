package pt.amane;


import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;
import pt.amane.infrastructure.configuration.WebServerConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@Tag("integrationTest")
public @interface AmqpTest {
}
