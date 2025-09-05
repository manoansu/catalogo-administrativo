package pt.amane.infrastructure.video.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record UpdateVideoResponse(@JsonProperty("id") String id) {
}
