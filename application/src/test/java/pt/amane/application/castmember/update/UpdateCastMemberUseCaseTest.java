package pt.amane.application.castmember.update;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.castmember.CastmemberID;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.utils.FixtureUtils;

class UpdateCastMemberUseCaseTest extends UseCaseTest {

  @InjectMocks
  private UpdateCastMemberUseCaseImpl useCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {

    //given
    final var aMember = CastMember.newCastmember("Vin Disel", CastMemberType.DIRECTOR);

    final var expectedName = FixtureUtils.name();
    final var expectedType = CastMemberType.ACTOR;
    final var expectedId = aMember.getId();

    final var aCommand = UpdateCastMemberCommand.with(expectedId.getValue(), expectedName, expectedType);

    when(castMemberGateway.findById(any()))
        .thenReturn(Optional.of(CastMember.with(aMember)));

    when(castMemberGateway.update(any()))
        .thenAnswer(returnsFirstArg());

    // when
    final var actualOutput = useCase.execute(aCommand);

    // then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

    verify(castMemberGateway).findById(eq(expectedId));

    verify(castMemberGateway).update(argThat(aUpdatedMember ->
        Objects.equals(expectedId, aUpdatedMember.getId())
            && Objects.equals(expectedName, aUpdatedMember.getName())
            && Objects.equals(expectedType, aUpdatedMember.getType())
            && Objects.equals(aMember.getCreatedAt(), aUpdatedMember.getCreatedAt())
            && aMember.getUpdatedAt().isBefore(aUpdatedMember.getUpdatedAt())
    ));
  }

  @Test
  void givenAInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
    // given
    final var aMember = CastMember.newCastmember("vin diesel", CastMemberType.DIRECTOR);

    final var expectedId = aMember.getId();
    final String expectedName = null;
    final var expectedType = CastMemberType.ACTOR;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );

    when(castMemberGateway.findById(any()))
        .thenReturn(Optional.of(aMember));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(castMemberGateway).findById(eq(expectedId));
    verify(castMemberGateway, times(0)).update(any());
  }

  @Test
  void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
    // given
    final var aMember = CastMember.newCastmember("vin diesel", CastMemberType.DIRECTOR);

    final var expectedId = aMember.getId();
    final var expectedName = FixtureUtils.name();
    final CastMemberType expectedType = null;

    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'type' should not be null";

    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );

    when(castMemberGateway.findById(any()))
        .thenReturn(Optional.of(aMember));

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);

    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

    verify(castMemberGateway).findById(eq(expectedId));
    verify(castMemberGateway, times(0)).update(any());
  }

  @Test
  void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
    // given
    CastMember.newCastmember("vin diesel", CastMemberType.DIRECTOR);

    final var expectedId = CastmemberID.from("123");
    final var expectedName = FixtureUtils.name();
    final var expectedType = FixtureUtils.CastMembers.type();

    final var expectedErrorMessage = "CastMember with ID 123 was not found";

    final var aCommand = UpdateCastMemberCommand.with(
        expectedId.getValue(),
        expectedName,
        expectedType
    );

    when(castMemberGateway.findById(any()))
        .thenReturn(Optional.empty());

    // when
    final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
      useCase.execute(aCommand);
    });

    // then
    Assertions.assertNotNull(actualException);

    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(castMemberGateway).findById(eq(expectedId));
    verify(castMemberGateway, times(0)).update(any());
  }

}