package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class UniqueTournament {
    @JsonProperty("category")
    Category category;
    @JsonProperty("name")
    String name;
    @JsonProperty("slug")
    String slug;
}
