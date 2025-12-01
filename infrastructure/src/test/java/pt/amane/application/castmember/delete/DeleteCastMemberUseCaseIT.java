package pt.amane.application.castmember.delete;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import pt.amane.IntegrationTest;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.infrastructure.castmember.persistence.CastMemberJpaEntity;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;

@IntegrationTest
class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());
        final var aMemberTwo = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

        final var expectedId = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMemberTwo));

        Assertions.assertEquals(2, this.castMemberRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        verify(castMemberGateway).deleteById(eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
        Assertions.assertTrue(this.castMemberRepository.existsById(aMemberTwo.getId().getValue()));
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        this.castMemberRepository.saveAndFlush(
                CastMemberJpaEntity.from(
                        CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type())
                )
        );

        final var expectedId = CastMemberID.from("123");

        Assertions.assertEquals(1, this.castMemberRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        verify(castMemberGateway).deleteById(eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();

        Assertions.assertEquals(1, this.castMemberRepository.count());

        doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(any());

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        verify(castMemberGateway).deleteById(eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
    }
}
