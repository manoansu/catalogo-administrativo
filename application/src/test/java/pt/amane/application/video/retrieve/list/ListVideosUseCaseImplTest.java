package pt.amane.application.video.retrieve.list;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.application.genre.retrieve.list.GenreListOutput;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.utils.FixtureUtils;
import pt.amane.domain.video.VideoGateway;
import pt.amane.domain.video.VideoPreview;
import pt.amane.domain.video.VideoSearchQuery;

class ListVideosUseCaseImplTest extends UseCaseTest {

  @InjectMocks
  private ListVideosUseCaseImpl useCase;

  @Mock
  private VideoGateway videoGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(videoGateway);
  }

  @Test
  void givenAValidQuery_whenCallsListVideos_shouldReturnVideos() {

    // given
    final var videos = List.of(
        new VideoPreview(FixtureUtils.video()),
        new VideoPreview(FixtureUtils.video())
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = videos.stream()
        .map(VideoListOutput::from)
        .toList();

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        videos
    );

    // Use doReturn() for better type inference with generics
    Mockito.doReturn(expectedPagination)
        .when(videoGateway)
        .findAll(Mockito.any(VideoSearchQuery.class));


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
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    Mockito.verify(videoGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
  }

  @Test
  void givenAValidQuery_whenCallsListVideosAndResultIsEmpty_shouldReturnGenres() {
    // given
    final var videos = List.<VideoPreview>of();

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var expectedItems = List.<GenreListOutput>of();

    final var expectedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        videos
    );

    Mockito.doReturn(expectedPagination)
        .when(videoGateway)
        .findAll(Mockito.any(VideoSearchQuery.class));

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
    final var actualOutput = useCase.execute(aQuery);

    // then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    Mockito.verify(videoGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
  }

  @Test
  void givenAValidQuery_whenCallsListVideosAndGatewayThrowsRandomError_shouldReturnException() {
    // given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "A";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";

    final var expectedErrorMessage = "Gateway error";

    Mockito.when(videoGateway.findAll(Mockito.any()))
        .thenThrow(new IllegalStateException(expectedErrorMessage));

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
    final var actualOutput = Assertions.assertThrows(
        IllegalStateException.class,
        () -> useCase.execute(aQuery)
    );

    // then
    Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

    Mockito.verify(videoGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
  }

}