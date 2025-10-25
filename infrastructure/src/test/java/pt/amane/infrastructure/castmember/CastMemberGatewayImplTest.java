package pt.amane.infrastructure.castmember;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pt.amane.Main;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.infrastructure.castmember.persistence.CastMemberJpaEntity;
import pt.amane.infrastructure.castmember.persistence.CastMemberRepository;

@ActiveProfiles("test-integration")
@SpringBootTest(classes = Main.class)
@Transactional
class CastMemberGatewayImplTest {

  @Autowired
  private CastMemberGatewayImpl castMemberGateway;

  @Autowired
  private CastMemberRepository castMemberRepository;

  @Test
  void testDependencies() {
    Assertions.assertNotNull(castMemberGateway);
    Assertions.assertNotNull(castMemberRepository);
  }

  @Test
  void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
    // given
    final var expectedName = FixtureUtils.name();
    final var expectedType = FixtureUtils.CastMembers.type();

    final var aMember = CastMember.newCastmember(expectedName, expectedType);
    final var expectedId = aMember.getId();

    Assertions.assertEquals(0, castMemberRepository.count());

    // when
    final var actualMember = castMemberGateway.create(CastMember.with(aMember));

    // then
    Assertions.assertEquals(1, castMemberRepository.count());

    Assertions.assertEquals(expectedId, actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertNotNull(actualMember.getCreatedAt());
    Assertions.assertNotNull(actualMember.getUpdatedAt());
    Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());

    final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
    Assertions.assertEquals(expectedName, persistedMember.getName());
    Assertions.assertEquals(expectedType, persistedMember.getType());
    Assertions.assertNotNull(persistedMember.getCreatedAt());
    Assertions.assertNotNull(persistedMember.getUpdatedAt());
    Assertions.assertEquals(persistedMember.getCreatedAt(), persistedMember.getUpdatedAt());
  }

  @Test
  void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
    // given
    final var expectedName = FixtureUtils.name();
    final var expectedType = CastMemberType.ACTOR;

    final var aMember = CastMember.newCastmember("vind", CastMemberType.DIRECTOR);
    final var expectedId = aMember.getId();

    final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    Assertions.assertEquals(1, castMemberRepository.count());
    Assertions.assertEquals("vind", currentMember.getName());
    Assertions.assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

    // when
    final var actualMember = castMemberGateway.update(
        CastMember.with(aMember).update(expectedName, expectedType)
    );

    // then
    Assertions.assertEquals(1, castMemberRepository.count());

    Assertions.assertEquals(expectedId, actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
    Assertions.assertTrue(aMember.getUpdatedAt().isBefore(actualMember.getUpdatedAt()));

    final var persistedMember = castMemberRepository.findById(expectedId.getValue()).get();

    Assertions.assertEquals(expectedId.getValue(), persistedMember.getId());
    Assertions.assertEquals(expectedName, persistedMember.getName());
    Assertions.assertEquals(expectedType, persistedMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), persistedMember.getCreatedAt());
    Assertions.assertTrue(aMember.getUpdatedAt().isBefore(persistedMember.getUpdatedAt()));
  }

  @Test
  void givenTwoCastMembersAndOnePersisted_whenCallsExistsByIds_shouldReturnPersistedID() {
    // given
    final var aMember = CastMember.newCastmember("Vin", CastMemberType.DIRECTOR);

    final var expectedItems = 1;
    final var expectedId = aMember.getId();

    Assertions.assertEquals(0, castMemberRepository.count());

    castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    // when
    final var actualMember = castMemberGateway.existsByIds(List.of(CastMemberID.from("123"), expectedId));

    // then
    Assertions.assertEquals(expectedItems, actualMember.size());
    Assertions.assertEquals(expectedId.getValue(), actualMember.get(0).getValue());
  }


  @Test
  void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
    // given
    final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

    castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    Assertions.assertEquals(1, castMemberRepository.count());

    // when
    castMemberGateway.deleteById(aMember.getId());

    // then
    Assertions.assertEquals(0, castMemberRepository.count());
  }

  @Test
  void givenAnInvalidId_whenCallsDeleteById_shouldBeIgnored() {
    // given
    final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

    castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    Assertions.assertEquals(1, castMemberRepository.count());

    // when
    castMemberGateway.deleteById(CastMemberID.from("123"));

    // then
    Assertions.assertEquals(1, castMemberRepository.count());
  }

  @Test
  void givenAValidCastMember_whenCallsFindById_shouldReturnIt() {
    // given
    final var expectedName = FixtureUtils.name();
    final var expectedType = FixtureUtils.CastMembers.type();

    final var aMember = CastMember.newCastmember(expectedName, expectedType);
    final var expectedId = aMember.getId();

    castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    Assertions.assertEquals(1, castMemberRepository.count());

    // when
    final var actualMember = castMemberGateway.findById(expectedId).get();

    // then
    Assertions.assertEquals(expectedId, actualMember.getId());
    Assertions.assertEquals(expectedName, actualMember.getName());
    Assertions.assertEquals(expectedType, actualMember.getType());
    Assertions.assertEquals(aMember.getCreatedAt(), actualMember.getCreatedAt());
    Assertions.assertEquals(aMember.getUpdatedAt(), actualMember.getUpdatedAt());
  }

  @Test
  void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
    // given
    final var aMember = CastMember.newCastmember(FixtureUtils.name(), FixtureUtils.CastMembers.type());

    castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

    Assertions.assertEquals(1, castMemberRepository.count());

    // when
    final var actualMember = castMemberGateway.findById(CastMemberID.from("123"));

    // then
    Assertions.assertTrue(actualMember.isEmpty());
  }

  @Test
  void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = castMemberGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());
  }

  @ParameterizedTest
  @CsvSource({
      "vin,0,10,1,1,Vin Diesel",
      "taran,0,10,1,1,Quentin Tarantino",
      "jas,0,10,1,1,Jason Momoa",
      "har,0,10,1,1,Kit Harington",
      "MAR,0,10,1,1,Martin Scorsese",
  })
  void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedName
  ) {
    // given
    mockMembers();

    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = castMemberGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
  }

  @ParameterizedTest
  @CsvSource({
      "name,asc,0,10,5,5,Jason Momoa",
      "name,desc,0,10,5,5,Vin Diesel",
      "createdAt,asc,0,10,5,5,Kit Harington",
      "createdAt,desc,0,10,5,5,Martin Scorsese",
  })
  void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedName
  ) {
    // given
    mockMembers();

    final var expectedTerms = "";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = castMemberGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedName, actualPage.items().get(0).getName());
  }

  @ParameterizedTest
  @CsvSource({
      "0,2,2,5,Jason Momoa;Kit Harington",
      "1,2,2,5,Martin Scorsese;Quentin Tarantino",
      "2,2,1,5,Vin Diesel",
  })
  void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedNames
  ) {
    // given
    mockMembers();

    final var expectedTerms = "";
    final var expectedSort = "name";
    final var expectedDirection = "asc";

    final var aQuery =
        new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    // when
    final var actualPage = castMemberGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

    int index = 0;
    for (final var expectedName : expectedNames.split(";")) {
      Assertions.assertEquals(expectedName, actualPage.items().get(index).getName());
      index++;
    }
  }

  private void mockMembers() {
    try {
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newCastmember("Kit Harington", CastMemberType.ACTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newCastmember("Vin Diesel", CastMemberType.ACTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newCastmember("Quentin Tarantino", CastMemberType.DIRECTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newCastmember("Jason Momoa", CastMemberType.ACTOR)));
        Thread.sleep(1);
        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(CastMember.newCastmember("Martin Scorsese", CastMemberType.DIRECTOR)));
    } catch (InterruptedException e) {
        // ignore
    }
  }
}