package pt.amane.infrastructure.video;

import java.time.Year;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pt.amane.Main;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.category.Category;
import pt.amane.domain.category.CategoryGateway;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.Genre;
import pt.amane.domain.genre.GenreGateway;
import pt.amane.domain.genre.GenreID;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.AudioVideoMedia;
import pt.amane.domain.video.ImageMedia;
import pt.amane.domain.video.Video;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoID;
import pt.amane.domain.video.VideoSearchQuery;
import pt.amane.infrastructure.video.persistence.VideoJpaEntity;
import pt.amane.infrastructure.video.persistence.VideoRepository;

@ActiveProfiles("test-integration")
@SpringBootTest(classes = Main.class)
@Transactional
class VideoGatewayImplTest {

  @Autowired
  private VideoGateway videoGateway;

  @Autowired
  private CastMemberGateway castMemberGateway;

  @Autowired
  private CategoryGateway categoryGateway;

  @Autowired
  private GenreGateway genreGateway;

  @Autowired
  private VideoRepository videoRepository;

  private CastMember wesley;
  private CastMember gabriel;

  private Category aulas;
  private Category lives;

  private Genre tech;
  private Genre business;

  @BeforeEach
  void setUp() {
    wesley = castMemberGateway.create(FixtureUtils.CastMembers.wesley());
    gabriel = castMemberGateway.create(FixtureUtils.CastMembers.gabriel());

    aulas = categoryGateway.create(FixtureUtils.Categories.aulas());
    lives = categoryGateway.create(FixtureUtils.Categories.lives());

    tech = genreGateway.create(FixtureUtils.Genres.tech());
    business = genreGateway.create(FixtureUtils.Genres.business());
  }

  @Test
  void testInjection() {
    Assertions.assertNotNull(videoGateway);
    Assertions.assertNotNull(castMemberGateway);
    Assertions.assertNotNull(categoryGateway);
    Assertions.assertNotNull(genreGateway);
    Assertions.assertNotNull(videoRepository);
  }

  @Test
  void givenAValidVideo_whenCallsCreate_shouldPersistIt() {

    // given
    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(aulas.getId());
    final var expectedGenres = Set.of(tech.getId());
    final var expectedMembers = Set.of(wesley.getId());

    final AudioVideoMedia expectedVideo =
        AudioVideoMedia.with("123", "video", "/media/video");

    final AudioVideoMedia expectedTrailer =
        AudioVideoMedia.with("123", "trailer", "/media/trailer");

    final ImageMedia expectedBanner =
        ImageMedia.with("123", "banner", "/media/banner");

    final ImageMedia expectedThumb =
        ImageMedia.with("123", "thumb", "/media/thumb");

    final ImageMedia expectedThumbHalf =
        ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

    final var aVideo = Video.newVideo(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers
        )
        .updateVideoMedia(expectedVideo)
        .updateTrailerMedia(expectedTrailer)
        .updateBannerMedia(expectedBanner)
        .updateThumbnailMedia(expectedThumb)
        .updateThumbnailHalfMedia(expectedThumbHalf);

    // when
    final var actualVideo = videoGateway.create(aVideo);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());

    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
    Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
    Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
    Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
    Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
    Assertions.assertNotNull(actualVideo.getCreatedAt());
    Assertions.assertNotNull(actualVideo.getUpdatedAt());

    final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

    Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
    Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
    Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
    Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
    Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
    Assertions.assertEquals(expectedRating, persistedVideo.getRating());
    Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
    Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
    Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
    Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
    Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
    Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
    Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
    Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    Assertions.assertNotNull(persistedVideo.getCreatedAt());
    Assertions.assertNotNull(persistedVideo.getUpdatedAt());
  }

  @Test
  void givenAValidVideoWithoutRelations_whenCallsCreate_shouldPersistIt() {
    // given
    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.<CategoryID>of();
    final var expectedGenres = Set.<GenreID>of();
    final var expectedMembers = Set.<CastMemberID>of();

    final var aVideo = Video.newVideo(
        expectedTitle,
        expectedDescription,
        expectedLaunchYear,
        expectedDuration,
        expectedOpened,
        expectedPublished,
        expectedRating,
        expectedCategories,
        expectedGenres,
        expectedMembers
    );

    // when
    final var actualVideo = videoGateway.create(aVideo);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());

    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertTrue(actualVideo.getVideo().isEmpty());
    Assertions.assertTrue(actualVideo.getTrailer().isEmpty());
    Assertions.assertTrue(actualVideo.getBanner().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnail().isEmpty());
    Assertions.assertTrue(actualVideo.getThumbnailHalf().isEmpty());
    Assertions.assertNotNull(actualVideo.getCreatedAt());
    Assertions.assertNotNull(actualVideo.getUpdatedAt());
    Assertions.assertEquals(actualVideo.getCreatedAt(), actualVideo.getUpdatedAt());

    final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

    Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
    Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
    Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
    Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
    Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
    Assertions.assertEquals(expectedRating, persistedVideo.getRating());
    Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
    Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
    Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
    Assertions.assertNull(persistedVideo.getVideo());
    Assertions.assertNull(persistedVideo.getTrailer());
    Assertions.assertNull(persistedVideo.getBanner());
    Assertions.assertNull(persistedVideo.getThumbnail());
    Assertions.assertNull(persistedVideo.getThumbnailHalf());
    Assertions.assertNotNull(persistedVideo.getCreatedAt());
    Assertions.assertNotNull(persistedVideo.getUpdatedAt());
    Assertions.assertEquals(persistedVideo.getCreatedAt(), persistedVideo.getUpdatedAt());
  }

  @Test
  void givenAValidVideo_whenCallsUpdate_shouldPersistIt() {
    // given
    final var aVideo = videoGateway.create(Video.newVideo(
        FixtureUtils.title(),
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(),
        Set.of(),
        Set.of()
    ));

    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(aulas.getId());
    final var expectedGenres = Set.of(tech.getId());
    final var expectedMembers = Set.of(wesley.getId());

    final AudioVideoMedia expectedVideo =
        AudioVideoMedia.with("123", "video", "/media/video");

    final AudioVideoMedia expectedTrailer =
        AudioVideoMedia.with("123", "trailer", "/media/trailer");

    final ImageMedia expectedBanner =
        ImageMedia.with("123", "banner", "/media/banner");

    final ImageMedia expectedThumb =
        ImageMedia.with("123", "thumb", "/media/thumb");

    final ImageMedia expectedThumbHalf =
        ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

    final var updatedVideo = Video.with(aVideo)
        .update(
            expectedTitle,
            expectedDescription,
            expectedLaunchYear,
            expectedDuration,
            expectedOpened,
            expectedPublished,
            expectedRating,
            expectedCategories,
            expectedGenres,
            expectedMembers
        )
        .updateVideoMedia(expectedVideo)
        .updateTrailerMedia(expectedTrailer)
        .updateBannerMedia(expectedBanner)
        .updateThumbnailMedia(expectedThumb)
        .updateThumbnailHalfMedia(expectedThumbHalf);

    // when
    final var actualVideo = videoGateway.update(updatedVideo);

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());

    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
    Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
    Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
    Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
    Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
    Assertions.assertNotNull(actualVideo.getCreatedAt());
    Assertions.assertTrue(actualVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));

    final var persistedVideo = videoRepository.findById(actualVideo.getId().getValue()).get();

    Assertions.assertEquals(expectedTitle, persistedVideo.getTitle());
    Assertions.assertEquals(expectedDescription, persistedVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, Year.of(persistedVideo.getYearLaunched()));
    Assertions.assertEquals(expectedDuration, persistedVideo.getDuration());
    Assertions.assertEquals(expectedOpened, persistedVideo.isOpened());
    Assertions.assertEquals(expectedPublished, persistedVideo.isPublished());
    Assertions.assertEquals(expectedRating, persistedVideo.getRating());
    Assertions.assertEquals(expectedCategories, persistedVideo.getCategoriesID());
    Assertions.assertEquals(expectedGenres, persistedVideo.getGenresID());
    Assertions.assertEquals(expectedMembers, persistedVideo.getCastMembersID());
    Assertions.assertEquals(expectedVideo.name(), persistedVideo.getVideo().getName());
    Assertions.assertEquals(expectedTrailer.name(), persistedVideo.getTrailer().getName());
    Assertions.assertEquals(expectedBanner.name(), persistedVideo.getBanner().getName());
    Assertions.assertEquals(expectedThumb.name(), persistedVideo.getThumbnail().getName());
    Assertions.assertEquals(expectedThumbHalf.name(), persistedVideo.getThumbnailHalf().getName());
    Assertions.assertNotNull(persistedVideo.getCreatedAt());
    Assertions.assertTrue(persistedVideo.getUpdatedAt().isAfter(aVideo.getUpdatedAt()));
  }

  @Test
  void givenAValidVideoId_whenCallsDeleteById_shouldDeleteIt() {
    // given
    final var aVideo = videoGateway.create(Video.newVideo(
        FixtureUtils.title(),
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(),
        Set.of(),
        Set.of()
    ));

    Assertions.assertEquals(1, videoRepository.count());

    final var anId = aVideo.getId();

    // when
    videoGateway.deleteById(anId);

    // then
    Assertions.assertEquals(0, videoRepository.count());
  }

  @Test
  void givenAnInvalidVideoId_whenCallsDeleteById_shouldDeleteIt() {
    // given
    videoGateway.create(Video.newVideo(
        FixtureUtils.title(),
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(),
        Set.of(),
        Set.of()
    ));

    Assertions.assertEquals(1, videoRepository.count());

    final var anId = VideoID.unique();

    // when
    videoGateway.deleteById(anId);

    // then
    Assertions.assertEquals(1, videoRepository.count());
  }

  @Test
  void givenAValidVideo_whenCallsFindById_shouldReturnIt() {
    // given
    final var expectedTitle = FixtureUtils.title();
    final var expectedDescription = FixtureUtils.Videos.description();
    final var expectedLaunchYear = Year.of(FixtureUtils.year());
    final var expectedDuration = FixtureUtils.duration();
    final var expectedOpened = FixtureUtils.bool();
    final var expectedPublished = FixtureUtils.bool();
    final var expectedRating = FixtureUtils.Videos.rating();
    final var expectedCategories = Set.of(aulas.getId());
    final var expectedGenres = Set.of(tech.getId());
    final var expectedMembers = Set.of(wesley.getId());

    final AudioVideoMedia expectedVideo =
        AudioVideoMedia.with("123", "video", "/media/video");

    final AudioVideoMedia expectedTrailer =
        AudioVideoMedia.with("123", "trailer", "/media/trailer");

    final ImageMedia expectedBanner =
        ImageMedia.with("123", "banner", "/media/banner");

    final ImageMedia expectedThumb =
        ImageMedia.with("123", "thumb", "/media/thumb");

    final ImageMedia expectedThumbHalf =
        ImageMedia.with("123", "thumbHalf", "/media/thumbHalf");

    final var aVideo = videoGateway.create(
        Video.newVideo(
                expectedTitle,
                expectedDescription,
                expectedLaunchYear,
                expectedDuration,
                expectedOpened,
                expectedPublished,
                expectedRating,
                expectedCategories,
                expectedGenres,
                expectedMembers
            )
            .updateVideoMedia(expectedVideo)
            .updateTrailerMedia(expectedTrailer)
            .updateBannerMedia(expectedBanner)
            .updateThumbnailMedia(expectedThumb)
            .updateThumbnailHalfMedia(expectedThumbHalf)
    );

    // when
    final var actualVideo = videoGateway.findById(aVideo.getId()).get();

    // then
    Assertions.assertNotNull(actualVideo);
    Assertions.assertNotNull(actualVideo.getId());

    Assertions.assertEquals(expectedTitle, actualVideo.getTitle());
    Assertions.assertEquals(expectedDescription, actualVideo.getDescription());
    Assertions.assertEquals(expectedLaunchYear, actualVideo.getLaunchedAt());
    Assertions.assertEquals(expectedDuration, actualVideo.getDuration());
    Assertions.assertEquals(expectedOpened, actualVideo.getOpened());
    Assertions.assertEquals(expectedPublished, actualVideo.getPublished());
    Assertions.assertEquals(expectedRating, actualVideo.getRating());
    Assertions.assertEquals(expectedCategories, actualVideo.getCategories());
    Assertions.assertEquals(expectedGenres, actualVideo.getGenres());
    Assertions.assertEquals(expectedMembers, actualVideo.getCastMembers());
    Assertions.assertEquals(expectedVideo.name(), actualVideo.getVideo().get().name());
    Assertions.assertEquals(expectedTrailer.name(), actualVideo.getTrailer().get().name());
    Assertions.assertEquals(expectedBanner.name(), actualVideo.getBanner().get().name());
    Assertions.assertEquals(expectedThumb.name(), actualVideo.getThumbnail().get().name());
    Assertions.assertEquals(expectedThumbHalf.name(), actualVideo.getThumbnailHalf().get().name());
  }

  @Test
  void givenAInvalidVideoId_whenCallsFindById_shouldEmpty() {
    // given
    videoGateway.create(Video.newVideo(
        FixtureUtils.title(),
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(),
        Set.of(),
        Set.of()
    ));

    final var anId = VideoID.unique();

    // when
    final var actualVideo = videoGateway.findById(anId);

    // then
    Assertions.assertTrue(actualVideo.isEmpty());
  }

  @Test
  void givenEmptyParams_whenCallFindAll_shouldReturnAllList() {
    // given
    mockVideos();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "title";
    final var expectedDirection = "asc";
    final var expectedTotal = 4;

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());
  }

  @Test
  void givenEmptyVideos_whenCallFindAll_shouldReturnEmptyList() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "title";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());
  }

  @Test
  void givenAValidCategory_whenCallFindAll_shouldReturnFilteredList() {
    // given
    mockVideos();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "title";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(aulas.getId()),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());

    Assertions.assertEquals("21.1 Implementação dos testes integrados do findAll", actualPage.items().get(0).title());
    Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(1).title());
  }

  @Test
  void givenAValidCastMember_whenCallFindAll_shouldReturnFilteredList() {
    // given
    mockVideos();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "title";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(wesley.getId()),
        Set.of(),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());

    Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
    Assertions.assertEquals("System Design no Mercado Livre na prática", actualPage.items().get(1).title());
  }

  @Test
  void givenAValidGenre_whenCallFindAll_shouldReturnFilteredList() {
    // given
    mockVideos();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "title";
    final var expectedDirection = "asc";
    final var expectedTotal = 1;

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(),
        Set.of(business.getId())
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());

    Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
  }

  @Test
  void givenAllParameters_whenCallFindAll_shouldReturnFilteredList() {
    // given
    mockVideos();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "empreendedorismo";
    final var expectedSort = "title";
    final var expectedDirection = "asc";
    final var expectedTotal = 1;

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(wesley.getId()),
        Set.of(aulas.getId()),
        Set.of(business.getId())
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedTotal, actualPage.items().size());

    Assertions.assertEquals("Aula de empreendedorismo", actualPage.items().get(0).title());
  }

  @ParameterizedTest
  @CsvSource({
      "0,2,2,4,21.1 Implementação dos testes integrados do findAll;Aula de empreendedorismo",
      "1,2,2,4,Não cometa esses erros ao trabalhar com Microsserviços;System Design no Mercado Livre na prática",
  })
  void givenAValidPaging_whenCallsFindAll_shouldReturnPaged(
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedVideos
  ) {
    // given
    mockVideos();

    final var expectedTerms = "";
    final var expectedSort = "title";
    final var expectedDirection = "asc";

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

    int index = 0;
    for (final var expectedTitle : expectedVideos.split(";")) {
      final var actualTitle = actualPage.items().get(index).title();
      Assertions.assertEquals(expectedTitle, actualTitle);
      index++;
    }
  }

  @ParameterizedTest
  @CsvSource({
      "createdAt,asc,0,10,4,4,System Design no Mercado Livre na prática",
      "createdAt,desc,0,10,4,4,Aula de empreendedorismo",
      "title,asc,0,10,4,4,21.1 Implementação dos testes integrados do findAll",
      "title,desc,0,10,4,4,System Design no Mercado Livre na prática",
  })
  void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
      final String expectedSort,
      final String expectedDirection,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedVideo
  ) {
    // given
    mockVideos();

    final var expectedTerms = "";

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedTotal, actualPage.total());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedVideo, actualPage.items().get(0).title());
  }

  @ParameterizedTest
  @CsvSource({
      "21,0,10,1,1,21.1 Implementação dos testes integrados do findAll",
      "system,0,10,1,1,System Design no Mercado Livre na prática",
      "empreendedorismo,0,10,1,1,Aula de empreendedorismo",
      "microsser,0,10,1,1,Não cometa esses erros ao trabalhar com Microsserviços",
  })
  void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
      final String expectedTerms,
      final int expectedPage,
      final int expectedPerPage,
      final int expectedItemsCount,
      final long expectedTotal,
      final String expectedVideo
  ) {
    // given
    mockVideos();

    final var expectedSort = "title";
    final var expectedDirection = "asc";

    final var aQuery = new VideoSearchQuery(
        expectedPage,
        expectedPerPage,
        expectedTerms,
        expectedSort,
        expectedDirection,
        Set.of(),
        Set.of(),
        Set.of()
    );

    // when
    final var actualPage = videoGateway.findAll(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualPage.currentPage());
    Assertions.assertEquals(expectedPerPage, actualPage.perPage());
    Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
    Assertions.assertEquals(expectedVideo, actualPage.items().get(0).title());
    Assertions.assertEquals(expectedTotal, actualPage.total());
  }

  private void mockVideos() {
    final var video1 = Video.newVideo(
        "System Design no Mercado Livre na prática",
        FixtureUtils.Videos.description(),
        Year.of(2022),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(lives.getId()),
        Set.of(tech.getId()),
        Set.of(wesley.getId(), gabriel.getId())
    );

    final var video2 = Video.newVideo(
        "Não cometa esses erros ao trabalhar com Microsserviços",
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(),
        Set.of(),
        Set.of()
    );

    final var video3 = Video.newVideo(
        "21.1 Implementação dos testes integrados do findAll",
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(aulas.getId()),
        Set.of(tech.getId()),
        Set.of(gabriel.getId())
    );

    final var video4 = Video.newVideo(
        "Aula de empreendedorismo",
        FixtureUtils.Videos.description(),
        Year.of(FixtureUtils.year()),
        FixtureUtils.duration(),
        FixtureUtils.bool(),
        FixtureUtils.bool(),
        FixtureUtils.Videos.rating(),
        Set.of(aulas.getId()),
        Set.of(business.getId()),
        Set.of(wesley.getId())
    );

    videoRepository.saveAllAndFlush(List.of(
        VideoJpaEntity.from(video1),
        VideoJpaEntity.from(video2),
        VideoJpaEntity.from(video3),
        VideoJpaEntity.from(video4)
    ));
  }
}
