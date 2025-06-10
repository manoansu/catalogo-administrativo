package pt.amane.application.castmember.retrieve.list;

import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import pt.amane.application.UseCaseTest;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.domain.utils.FixtureUtils;

class ListCastMembersUseCaseTest extends UseCaseTest {

  @InjectMocks
  private ListCastMembersUseCaseImpl useCase;

  @Mock
  private CastMemberGateway castMemberGateway;

  @Override
  protected List<Object> getMocks() {
    return List.of(castMemberGateway);
  }

  @Test
  void givenAValidQuery_WhenCallsListCastMembers_shouldReturnAll() {

    //given
    final var members = List.of(
        CastMember.newCastmember(FixtureUtils.name(), CastMemberType.DIRECTOR),
        CastMember.newCastmember(FixtureUtils.name(), CastMemberType.ACTOR)
    );

    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "Algo";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 2;

    final var expectedItems = members.stream().map(ListCastMembersOutput::from).toList();

    final var expecedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        members
    );

    Mockito.when(castMemberGateway.findAll(Mockito.any()))
        .thenReturn(expecedPagination);

    final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    //when
    final var actualOutput = useCase.execute(aQuery);

    //then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    Mockito.verify(castMemberGateway).findAll(Mockito.any());

  }

  @Test
  void givenAValidQuery_WhenCallsListCastMembersAndIsEmpty_shouldReturn() {

    //given
    final var expectedPage = 0;
    final var expectedPerPage = 10;
    final var expectedTerms = "";
    final var expectedSort = "createdAt";
    final var expectedDirection = "asc";
    final var expectedTotal = 0;

    final var members = List.<CastMember>of();
    final var expectedItems = List.<ListCastMembersOutput>of();

    final var expecedPagination = new Pagination<>(
        expectedPage,
        expectedPerPage,
        expectedTotal,
        members
    );

    Mockito.when(castMemberGateway.findAll(Mockito.any()))
        .thenReturn(expecedPagination);

    final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

    //when
    final var actualOutput = useCase.execute(aQuery);

    //then
    Assertions.assertEquals(expectedPage, actualOutput.currentPage());
    Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
    Assertions.assertEquals(expectedTotal, actualOutput.total());
    Assertions.assertEquals(expectedItems, actualOutput.items());

    Mockito.verify(castMemberGateway).findAll(Mockito.any());
  }

@Test
void givenAValidQuery_WhenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {

    //given
  final var expectedPage = 0;
  final var expectedPerPage = 10;
  final var expectedTerms = "";
  final var expectedSort = "createdAt";
  final var expectedDirection = "asc";

  final var expectedErrorMessage = "Gateway error";

  Mockito.when(castMemberGateway.findAll(Mockito.any()))
      .thenThrow(new IllegalStateException(expectedErrorMessage));


  final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

  //when
  final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> {
    useCase.execute(aQuery);
  });

  //then
  Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

  Mockito.verify(castMemberGateway).findAll(Mockito.any());

}

}