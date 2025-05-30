package pt.amane.infrastructure.category.model;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import pt.amane.JacksonTest;

@JacksonTest
class UpdateCategoryRequestTest {

  @Autowired
  private JacksonTester<UpdateCategoryRequest> json;

  @Test
  void testUnmarshall() throws Exception {
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = true;

    final var json = """
        {
          "name": "%s",
          "description": "%s",
          "is_active": %s
        }    
        """.formatted(expectedName, expectedDescription, expectedIsActive);

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("description", expectedDescription)
        .hasFieldOrPropertyWithValue("active", expectedIsActive);
  }
}