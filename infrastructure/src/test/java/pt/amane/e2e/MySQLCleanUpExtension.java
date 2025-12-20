package pt.amane.e2e;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.testcontainers.containers.MySQLContainer;

public class MySQLCleanUpExtension implements BeforeAllCallback {

  // Initialize the container, that we will up to start container for test.
  public static final MySQLContainer<?> MYSQL_CONTAINER =
      new MySQLContainer<>("mysql:8.0.27")
          .withPassword("123456")
          .withUsername("root")
          .withDatabaseName("adm_videos");

  @Override
  public void beforeAll(ExtensionContext context) {
    MYSQL_CONTAINER.start();
  }

  public static MySQLContainer<?> getContainer() {
    return MYSQL_CONTAINER;
  }
}