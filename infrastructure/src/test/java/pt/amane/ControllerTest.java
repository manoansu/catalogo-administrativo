package pt.amane;

import org.junit.jupiter.api.Tag;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.*;
import pt.amane.infrastructure.configuration.ObjectMapperConfig;
import pt.amane.infrastructure.configuration.WebServerConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@AutoConfigureMockMvc
@Import(ObjectMapperConfig.class)
@Tag("integrationTest")
public @interface ControllerTest {

}
