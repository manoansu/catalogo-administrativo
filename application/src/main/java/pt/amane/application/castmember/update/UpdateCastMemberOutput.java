package pt.amane.application.castmember.update;

import pt.amane.domain.castmember.CastMember;
import pt.amane.domain.castmember.CastmemberID;

public record UpdateCastMemberOutput(
    String id
) {

  public static UpdateCastMemberOutput from(final CastMember castMember) {
    return new UpdateCastMemberOutput(
        castMember.getId().getValue());
  }

  public static UpdateCastMemberOutput from(final CastmemberID castmemberID) {
    return new UpdateCastMemberOutput(castmemberID.getValue());
  }
}
