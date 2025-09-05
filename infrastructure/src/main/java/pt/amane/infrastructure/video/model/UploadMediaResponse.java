package pt.amane.infrastructure.video.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import pt.amane.domain.video.VideoMediaType;

public record UploadMediaResponse(
        @JsonProperty("video_id") String videoId,
        @JsonProperty("media_type") VideoMediaType mediaType
) {
}
