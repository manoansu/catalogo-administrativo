package pt.amane.infrastructure.castmember.model;

import pt.amane.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
    String name,
    CastMemberType type

) {

}
