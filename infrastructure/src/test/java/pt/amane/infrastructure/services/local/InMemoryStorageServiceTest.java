package pt.amane.infrastructure.services.local;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.VideoMediaType;

class InMemoryStorageServiceTest {

  private InMemoryStorageService target = new InMemoryStorageService();

  @BeforeEach
  void setUp() {
    target.clear();
  }

  @Test
  void givenValidResource_whenCallsStore_shouldStoreIt() {

    //given
    final var expectedResource = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final var expectedId = "item";

    target.store(expectedId, expectedResource);

    //when
    final var actualContent = this.target.storage().get(expectedId);

    //then
    Assertions.assertEquals(expectedResource, actualContent);
  }

  @Test
  void givenResource_whenCallsGet_shouldRetrieveIt() {

    //given
    final var expectedResource = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final var expectedId = "item";

    this.target.storage().put(expectedId, expectedResource);

    //when
    final var actualContent = target.get(expectedId).get();

    //then
    Assertions.assertEquals(expectedResource, actualContent);
  }

  @Test
  void givenInvalidResource_whenCallsGet_shouldRetrieveEmpty() {

    //given
    final var expectedResource = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);
    final var expectedId = "jajaja";

    this.target.storage().put("item", expectedResource);

    //when
    final var actualContent = target.get(expectedId);

    //then
    Assertions.assertTrue(actualContent.isEmpty());
  }

  @Test
  void givenPrefix_whenCallsList_shouldRetrieveAll() {

    //given
    final var expectedResource = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);

    final var expectedIds = List.of("item1", "item2");

    this.target.storage().put("item1", expectedResource);
    this.target.storage().put("item2", expectedResource);

    //when
    final var actualContent = target.list("it");

    //then
    Assertions.assertTrue(
        expectedIds.size() == actualContent.size()
            && expectedIds.containsAll(actualContent)
    );
  }

  @Test
  void givenResource_whenCallsDeleteAll_shouldEmptyStorage() {

    //given
    final var expectedResource = FixtureUtils.Videos.resource(VideoMediaType.THUMBNAIL);

    final var expectedIds = List.of("item1", "item2");

    this.target.storage().put("item1", expectedResource);
    this.target.storage().put("item2", expectedResource);

    //when
    target.deleteAll(expectedIds);

    //then
    Assertions.assertTrue(this.target.storage().isEmpty());
  }
}