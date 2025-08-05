package pt.amane.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Collections;
import java.util.List;

public record UpdateGenreRequest(
    @JsonProperty("name") String name,
    @JsonProperty("categories_id") List<String> categories,
    @JsonProperty("is_active") Boolean active
) {

  public Boolean isActive() {
    return active != null && active;
  }

  public List<String> categories() {
    return categories != null ? categories : Collections.emptyList();
  }
}
