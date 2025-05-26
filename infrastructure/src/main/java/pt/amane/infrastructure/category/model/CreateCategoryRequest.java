package pt.amane.infrastructure.category.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateCategoryRequest(
    @JsonProperty("name") String name,
    @JsonProperty("description") String description,
    @JsonProperty("is_active") Boolean active
) {

}
