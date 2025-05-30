package pt.amane.infrastructure.category.model;


import java.time.Instant;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

@JsonTest
class CategoryResponseTest {

  @Autowired
  private JacksonTester<CategoryResponse> json;

  @Test
  void testMarshall() throws Exception {
    final var expectedId = "123";
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var response = new CategoryResponse(
        expectedId,
        expectedName,
        expectedDescription,
        expectedIsActive,
        expectedCreatedAt,
        expectedUpdatedAt,
        expectedDeletedAt
    );

    final var actualJson = this.json.write(response);

    Assertions.assertThat(actualJson)
        .hasJsonPathValue("$.id", expectedId)
        .hasJsonPathValue("$.name", expectedName)
        .hasJsonPathValue("$.description", expectedDescription)
        .hasJsonPathValue("$.is_active", expectedIsActive)
        .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
        .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
        .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());
  }

  @Test
  void testUnmarshall() throws Exception {
    final var expectedId = "123";
    final var expectedName = "Filmes";
    final var expectedDescription = "A categoria mais assistida";
    final var expectedIsActive = false;
    final var expectedCreatedAt = Instant.now();
    final var expectedUpdatedAt = Instant.now();
    final var expectedDeletedAt = Instant.now();

    final var json = """
        {
          "id": "%s",
          "name": "%s",
          "description": "%s",
          "is_active": %s,
          "created_at": "%s",
          "deleted_at": "%s",
          "updated_at": "%s"
        }    
        """.formatted(
        expectedId,
        expectedName,
        expectedDescription,
        expectedIsActive,
        expectedCreatedAt.toString(),
        expectedDeletedAt.toString(),
        expectedUpdatedAt.toString()
    );

    final var actualJson = this.json.parse(json);

    Assertions.assertThat(actualJson)
        .hasFieldOrPropertyWithValue("id", expectedId)
        .hasFieldOrPropertyWithValue("name", expectedName)
        .hasFieldOrPropertyWithValue("description", expectedDescription)
        .hasFieldOrPropertyWithValue("active", expectedIsActive)
        .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
        .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
        .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
  }

}