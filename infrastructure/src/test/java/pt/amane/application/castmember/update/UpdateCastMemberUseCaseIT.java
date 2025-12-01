package pt.amane.application.castmember.update;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import pt.amane.IntegrationTest;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.exception.NotificationException;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;

@IntegrationTest
public class UpdateCastMemberUseCaseIT {

    @Autowired
    private UpdateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockBean
    private CastMemberGateway castMemberGateway;

    @Test
    void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // given
        final var aMember = CastMember.newCastmember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = FixtureUtils.name();
        final var expectedType = CastMemberType.ACTOR;

        doReturn(Optional.of(CastMember.with(aMember)))
                .when(castMemberGateway).findById(any());
        // Stub the update method to return the updated CastMember
        doReturn(aMember.update(expectedName, expectedType))
                .when(castMemberGateway).update(any());

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway).update(any());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aMember = CastMember.newCastmember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        doReturn(Optional.of(CastMember.with(aMember)))
                .when(castMemberGateway).findById(any());

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> { // Corrected exception type
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // given
        final var aMember = CastMember.newCastmember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.getId();
        final var expectedName = FixtureUtils.name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        doReturn(Optional.of(CastMember.with(aMember)))
                .when(castMemberGateway).findById(any());

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // given
        final var expectedId = CastMemberID.from("123");
        final var expectedName = FixtureUtils.name();
        final var expectedType = FixtureUtils.CastMembers.type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        doReturn(Optional.empty())
                .when(castMemberGateway).findById(any());

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedType
        );

        // when
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> {
            useCase.execute(aCommand);
        });

        // then
        Assertions.assertNotNull(actualException);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway, times(0)).update(any());
    }
}
