package pt.amane.domain.video;

import java.util.Set;
import pt.amane.domain.castmember.CastMemberID;

public record VideoSearchQuery(
    int page,
    int perPage,
    String terms,
    String sort,
    String direction,
    Set<CastMemberID> categoryId,
    Set<CastMemberID> genreId,
    Set<CastMemberID> castMemberId
) {


}
