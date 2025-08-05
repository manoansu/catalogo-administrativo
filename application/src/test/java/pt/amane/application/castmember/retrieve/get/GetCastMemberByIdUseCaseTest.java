package pt.amane.application.castmember.retrieve.get;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.exception.NotFoundException;
import pt.amane.domain.utils.FixtureUtils;

class GetCastMemberByIdUseCaseTest extends UseCaseTest {

  @InjectMocks
  private GetCastMemberByIdUseCaseImpl useCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  void givenAValidId_whenCallsGetCastMember_shouldReturnId() {

    //given
    final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

    final var expectedId = aMember.getId();
    final var expectedName = aMember.getName();
    final var expectedType = aMember.getType();
    final var expectedCreatedAt = aMember.getCreatedAt();
    final var expectedUpdatedAt = aMember.getUpdatedAt();

    when(castMemberGateway.findById(any()))
        .thenReturn(Optional.of(CastMember.with(aMember)));

    //when
    final var actualOutput = useCase.execute(expectedId.getValue());

    //when
    Assertions.assertEquals(expectedId.getValue(), actualOutput.id());
    Assertions.assertEquals(expectedName, actualOutput.name());
    Assertions.assertEquals(expectedType, actualOutput.type());
    Assertions.assertEquals(expectedCreatedAt, actualOutput.createdAt());
    Assertions.assertEquals(expectedUpdatedAt, actualOutput.updatedAt());

  }

  @Test
  void givenAInvalidName_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {

    //given
    final var expectedId = CastMemberID.from("123");
    final var expectedErrorMessage = "CastMember with ID 123 was not found";

    when(castMemberGateway.findById(any()))
        .thenReturn(Optional.empty());

    //when
    final var actualException = Assertions.assertThrows(NotFoundException.class,
        ()-> useCase.execute(expectedId.getValue()));

    //then
    Assertions.assertNotNull(actualException);
    Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

    verify(castMemberGateway).findById(eq(expectedId));

  }

}