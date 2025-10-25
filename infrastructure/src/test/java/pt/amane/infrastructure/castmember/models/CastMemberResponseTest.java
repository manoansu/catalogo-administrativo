package pt.amane.infrastructure.castmember.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;

import java.time.Instant;
import pt.amane.JacksonTest;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.infrastructure.castmember.model.CastMemberResponse;

@JacksonTest
class CastMemberResponseTest {

    @Autowired
    private JacksonTester<CastMemberResponse> json;

    @Test
    void testMarshall() throws Exception {
        final var expectedId = "123";
        final var expectedName = FixtureUtils.name();
        final var expectedType = FixtureUtils.CastMembers.type().name();
        final var expectedCreatedAt = Instant.now().toString();
        final var expectedUpdatedAt = Instant.now().toString();

        final var response = new CastMemberResponse(
                expectedId,
                expectedName,
                expectedType,
                expectedCreatedAt,
                expectedUpdatedAt
        );

        final var actualJson = this.json.write(response);

        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.type", expectedType)
                .hasJsonPathValue("$.created_at", expectedCreatedAt)
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt);
    }
}