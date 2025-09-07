package pt.amane.infrastructure.video.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;
import java.util.Set;

public record UpdateVideoRequest(
        @JsonProperty("title") String title,
        @JsonProperty("description") String description,
        @JsonProperty("duration") BigDecimal duration,
        @JsonProperty("year_launched") Integer yearLaunched,
        @JsonProperty("opened") Boolean opened,
        @JsonProperty("published") Boolean published,
        @JsonProperty("rating") String rating,
        @JsonProperty("cast_members") Set<String> castMembers,
        @JsonProperty("categories") Set<String> categories,
        @JsonProperty("genres") Set<String> genres
) {
}
