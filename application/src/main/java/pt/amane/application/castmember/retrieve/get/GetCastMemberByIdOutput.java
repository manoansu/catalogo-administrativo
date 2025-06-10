package pt.amane.application.castmember.retrieve.get;

import java.time.Instant;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.castmember.CastMember;

public record GetCastMemberByIdOutput(
    String id,
    String name,
    CastMemberType type,
    Instant createdAt,
    Instant updatedAt
) {

  public static GetCastMemberByIdOutput from(final CastMember aMember) {
    return new GetCastMemberByIdOutput(
        aMember.getId().getValue(),
        aMember.getName(),
        aMember.getType(),
        aMember.getCreatedAt(),
        aMember.getUpdatedAt()
    );
  }
}
