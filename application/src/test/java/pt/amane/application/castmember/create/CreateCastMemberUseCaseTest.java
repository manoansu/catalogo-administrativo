package pt.amane.application.castmember.create;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.utils.FixtureUtils;

class CreateCastMemberUseCaseTest extends UseCaseTest {

  @InjectMocks
  private CreateCastMemberUseCaseImpl useCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {

    //given
    final var expectedName = FixtureUtils.name();
    final var expectedType = FixtureUtils.CastMembers.type();

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    when(castMemberGateway.create(any()))
        .thenAnswer(returnsFirstArg());

    //when
    final var actualOutput = useCase.execute(aCommand);

    //then
    Assertions.assertNotNull(actualOutput);
    Assertions.assertNotNull(actualOutput.id());

    verify(castMemberGateway).create(argThat(aMember ->
        Objects.nonNull(aMember.getId())
            && Objects.equals(expectedName, aMember.getName())
            && Objects.equals(expectedType, aMember.getType())
            && Objects.nonNull(aMember.getCreatedAt())
            && Objects.nonNull(aMember.getUpdatedAt())
    ));

  }

  @Test
  void givenAInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {

    //given
    final String expectedName = null;
    final var expectedType = FixtureUtils.CastMembers.type();
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'name' should not be null";

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    // when
    final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
      useCase.execute(aCommand);
    });

    //then
    Assertions.assertNotNull(actualException);
    Assertions.assertNotNull(actualException.getErrors());
    Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
    Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

  }

  @Test
  void givenAInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {

    //given
    final var expectedName = FixtureUtils.name();
    final CastMemberType expectedType = null;
    final var expectedErrorCount = 1;
    final var expectedErrorMessage = "'type' should not be null";

    final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

    //when
    final var actualException = Assertions.assertThrows(NotificationException.class, () ->
      useCase.execute(aCommand));

      //then
      Assertions.assertNotNull(actualException);
      Assertions.assertNotNull(actualException.getErrors());
      Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
      Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
    }

  }