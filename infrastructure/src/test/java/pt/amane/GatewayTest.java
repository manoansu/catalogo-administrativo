package pt.amane;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pt.amane.Main;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@ComponentScan(
    basePackages = "pt.amane",
    useDefaultFilters = false,
    includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway")
    }
)
@DataJpaTest
@ExtendWith(CleanUpExtension.class)
@Tag("integrationTest")
public @interface GatewayTest {
}


//@Target(ElementType.TYPE)
//@Retention(RetentionPolicy.RUNTIME)
//@Inherited
//@ActiveProfiles("test-integration")
//@SpringBootTest(
//    classes = Main.class,
//    properties = {
//        "spring.datasource.url=jdbc:h2:mem:testdb;MODE=MYSQL;DB_CLOSE_DELAY=-1",
//        "spring.datasource.username=sa",
//        "spring.datasource.password=",
//        "spring.datasource.driverClassName=org.h2.Driver",
//        "spring.jpa.hibernate.ddl-auto=validate",
//        "spring.flyway.enabled=true"
//    }
//)
//@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
//@Tag("integrationTest")
//public @interface GatewayTest {
//
//}
