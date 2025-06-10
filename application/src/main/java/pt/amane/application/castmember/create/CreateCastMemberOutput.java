package pt.amane.application.castmember.create;

import pt.amane.domain.castmember.CastMember;

public record CreateCastMemberOutput(
    String id
) {

  public static CreateCastMemberOutput with(final String id) {
    return new CreateCastMemberOutput(id);
  }

  public static CreateCastMemberOutput from(final CastMember castmember) {
    return new CreateCastMemberOutput(castmember.getId().getValue());
  }
}
