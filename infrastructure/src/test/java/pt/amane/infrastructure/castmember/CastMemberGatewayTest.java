package pt.amane.infrastructure.castmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import pt.amane.Main;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;

@ActiveProfiles("test-integration")
@SpringBootTest(classes = Main.class)
class CastMemberGatewayTest {

  @Autowired
  private CastMemberGateway castMemberGateway;

  @Autowired
  private CastMemberRepository castMemberRepository;


  @Test
  void testDependencies() {
    Assertions.assertNotNull(castMemberGateway);
    Assertions.assertNotNull(castMemberRepository);
  }

  @Test
  void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {

    //given
    final var expectedName = FixtureUtils.name();
    final var expectedType = FixtureUtils.CastMembers.type();

    final var aMember = CastMember.newCastmember(expectedName, expectedType);
    final var expectedId = aMember.getId();

    Assertions.assertEquals(0, castMemberRepository.count());

    //when
    final var actualMember = castMemberGateway.create(CastMember.with(aMember));

    //then
    Assertions.assertEquals(1, castMemberRepository.count());

    Assertions.assertEquals(expectedId, actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
    Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());

    final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
    Assertions.assertEquals(expectedName, persistedMember.getName());
    Assertions.assertEquals(expectedType, persistedMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
    Assertions.assertEquals(aMember.getUpdatedAt(), persistedMember.getUpdatedAt());

  }
}