package pt.amane.infrastructure.castmember.model;

import pt.amane.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(
    String name,
    CastMemberType type
) {

}
