package pt.amane.domain.category;

public record CategorySearchByQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction
) {

}
