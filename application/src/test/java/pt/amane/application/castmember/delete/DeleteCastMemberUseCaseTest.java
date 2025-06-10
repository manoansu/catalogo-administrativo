package pt.amane.application.castmember.delete;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastmemberID;
import pt.amane.domain.utils.FixtureUtils;

class DeleteCastMemberUseCaseTest extends UseCaseTest {

  @InjectMocks
  private DeleteCastMemberUseCaseImpl useCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {

    //given
    final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

    final var expectedId = aMember.getId();

    doNothing()
        .when(castMemberGateway).deleteById(any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteById(eq(expectedId));
  }

  @Test
  void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {

    //given
    final var expectedId = CastmemberID.from("123");

    doNothing()
        .when(castMemberGateway).deleteById(any());

    // when
    Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteById(eq(expectedId));
  }

  @Test
  void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {

    // given
    final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

    final var expectedId = aMember.getId();

    doThrow(new IllegalStateException("Gateway error"))
        .when(castMemberGateway).deleteById(any());

    // when
    Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

    // then
    verify(castMemberGateway).deleteById(eq(expectedId));

  }




}