package pt.amane.application.castmember.update;

import java.time.Instant;
import pt.amane.domain.castmember.CastMemberType;
import pt.amane.domain.utils.InstantUtils;

public record UpdateCastMemberCommand(
    String id,
    String name,
    CastMemberType type
) {

  public static UpdateCastMemberCommand with(
      final String id,
      final String name,
      final CastMemberType type)
  {
    final var now = InstantUtils.now();
    return new UpdateCastMemberCommand(id, name, type);
  }

}
