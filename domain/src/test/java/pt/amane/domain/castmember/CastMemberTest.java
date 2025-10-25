package pt.amane.domain.castmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import pt.amane.UnitTest;
import pt.amane.domain.exception.NotificationException;

class CastMemberTest extends UnitTest {


  @Test
  void givenAValidParams_whenCallsNewCastmember_thenInstantiateACastmember() {
    final var expectedName = "Vin Diesel";
    final var expectedType = CastMemberType.ACTOR;

    final var actualCastmember = CastMember.newCastmember(expectedName, expectedType);

    Assertions.assertNotNull(actualCastmember);
    Assertions.assertNotNull(actualCastmember.getId());
    Assertions.assertEquals(expectedName, actualCastmember.getName());
    Assertions.assertEquals(expectedType, actualCastmember.getType());
    Assertions.assertNotNull(actualCastmember.getCreatedAt());
    Assertions.assertNotNull(actualCastmember.getUpdatedAt());
    Assertions.assertEquals(actualCastmember.getCreatedAt(), actualCastmember.getUpdatedAt());
  }

  @Test
  void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
    final String expectedName = null;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' should not be null";
    final var expectedType = CastMemberType.ACTOR;

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> CastMember.newCastmember(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

  }

  @Test
  void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
    final String expectedName = "";
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' should not be empty";
    final var expectedType = CastMemberType.ACTOR;

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> CastMember.newCastmember(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
    final String expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
    final var expectedType = CastMemberType.ACTOR;

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> CastMember.newCastmember(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAInvalidNameWithLengthLessThan3_whenCallsNewMember_shouldReceiveANotification() {
    final String expectedName = "An";
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' must be between 3 and 255 characters";
    final var expectedType = CastMemberType.ACTOR;

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> CastMember.newCastmember(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
    final String expectedName = "Vin Disel";
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'type' should not be null";
    final CastMemberType expectedType = null;

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> CastMember.newCastmember(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

  }

  @Test
  void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
    final var expectedName = "Vin Diesel";
    final var expectedType = CastMemberType.ACTOR;

    final var aCastMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(aCastMember);
    Assertions.assertNotNull(aCastMember.getId());

    final var actualId = aCastMember.getId();
    final var actualCreatedAt = aCastMember.getCreatedAt();
    final var actualUpdatedAt = aCastMember.getUpdatedAt();

    aCastMember.update(expectedName, expectedType);

    Assertions.assertEquals(actualId, aCastMember.getId());
    Assertions.assertEquals(expectedName, aCastMember.getName());
    Assertions.assertEquals(expectedType,aCastMember.getType());
    Assertions.assertEquals(actualCreatedAt,aCastMember.getCreatedAt());
    Assertions.assertTrue(actualUpdatedAt.isBefore(aCastMember.getUpdatedAt()));

  }

  @Test
  void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
    final String expectedName = null;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' should not be null";

    final var aCastMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(aCastMember);
    Assertions.assertNotNull(aCastMember.getId());

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> aCastMember.update(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
    final String expectedName = "";
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' should not be empty";

    final var aCastMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(aCastMember);
    Assertions.assertNotNull(aCastMember.getId());

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> aCastMember.update(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAValidCastMember_whenCallUpdateWithLengthMoreThan255_shouldReceiveNotification() {
    final String expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos governamentais com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' must be between 3 and 255 characters";

    final var aCastMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(aCastMember);
    Assertions.assertNotNull(aCastMember.getId());

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> aCastMember.update(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAValidCastMember_whenCallUpdateWithLengthLessThan3_shouldReceiveNotification() {
    final String expectedName = "Vi";
    final var expectedType = CastMemberType.ACTOR;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'name' must be between 3 and 255 characters";

    final var aCastMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(aCastMember);
    Assertions.assertNotNull(aCastMember.getId());

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> aCastMember.update(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());
  }

  @Test
  void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
    final String expectedName = "Vin Diesel";
    final CastMemberType expectedType = null;
    final var expectedErrorCount = 1;
    final String expectedErrorMessage = "'type' should not be null";

    final var aCastMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    Assertions.assertNotNull(aCastMember);
    Assertions.assertNotNull(aCastMember.getId());

    final var notification = Assertions.assertThrows(NotificationException.class,
        () -> aCastMember.update(expectedName, expectedType));

    Assertions.assertNotNull(notification);
    Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, notification.getErrors().get(0).message());

  }



}