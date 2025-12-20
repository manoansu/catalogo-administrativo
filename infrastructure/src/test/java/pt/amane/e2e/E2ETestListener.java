package pt.amane.e2e;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import pt.amane.E2ETest;

@E2ETest
public abstract class E2ETestListener {

    /**
     * This method its important to rewrite the spring properties dynamically based with the container done.
     * @param registry
     */
    @DynamicPropertySource
    public static void setDatasourceProperties(final DynamicPropertyRegistry registry) {
        final var container = MySQLCleanUpExtension.getContainer();
        final var jdbcUrl = "jdbc:mysql://%s:%s/%s".formatted(
                container.getHost(),
                container.getMappedPort(3306),
                container.getDatabaseName());

        registry.add("spring.datasource.url", () -> jdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }
}