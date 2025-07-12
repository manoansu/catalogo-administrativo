package pt.amane.infrastructure.castmember.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CastMemberListResponse(
    @JsonProperty("id") String id,
    @JsonProperty("name") String name,
    @JsonProperty("type") String type,
    @JsonProperty("created_at") String createdAt
) {

}
