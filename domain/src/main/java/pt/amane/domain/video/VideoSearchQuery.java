package pt.amane.domain.video;

import java.util.Set;
import pt.amane.domain.castmember.CastMemberID;
import pt.amane.domain.category.CategoryID;
import pt.amane.domain.genre.GenreID;

public record VideoSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction,
    Set<CastMemberID> categoryId,
    Set<CategoryID> genreId,
    Set<GenreID> castMemberId
) {


}
