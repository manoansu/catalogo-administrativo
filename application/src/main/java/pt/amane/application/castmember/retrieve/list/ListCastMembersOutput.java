package pt.amane.application.castmember.retrieve.list;

import java.time.Instant;
import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberType;

public record ListCastMembersOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt
) {

  public static ListCastMembersOutput from(final CastMember castMember) {
    return new ListCastMembersOutput(
        castMember.getId().getValue(),
        castMember.getName(),
        castMember.getType(),
        castMember.getCreatedAt()
    );
  }
}
