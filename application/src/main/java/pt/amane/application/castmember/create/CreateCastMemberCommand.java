package pt.amane.application.castmember.create;

import pt.amane.domain.castmember.CastMemberType;

public record CreateCastMemberCommand(
    String name,
    CastMemberType type
) {

  public static CreateCastMemberCommand with(final String aName, final CastMemberType aType) {
    return new CreateCastMemberCommand(aName, aType);
  }
}
