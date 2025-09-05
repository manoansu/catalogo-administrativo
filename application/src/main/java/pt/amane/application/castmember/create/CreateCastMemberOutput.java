package pt.amane.application.castmember.create;

import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastMemberID;

public record CreateCastMemberOutput(
    String id
) {

  public static CreateCastMemberOutput from(final CastMemberID id) {
    return new CreateCastMemberOutput(id.getValue());
  }

  public static CreateCastMemberOutput from(final CastMember castmember) {
    return new CreateCastMemberOutput(castmember.getId().getValue());
  }
}
