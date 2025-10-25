package pt.amane;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pt.amane.infrastructure.configuration.WebServerConfig;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@SpringBootTest(classes = WebServerConfig.class)
@ExtendWith(CleanUpExtension.class)
@Tag("integrationTest")
public @interface IntegrationTest {
}

//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Inherited
//@ActiveProfiles("test-integration")
//@SpringBootTest(classes = Main.class)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
//@Tag("integrationTest")
//public @interface IntegrationTest {
//}
