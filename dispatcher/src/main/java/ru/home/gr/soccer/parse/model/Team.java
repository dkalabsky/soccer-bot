package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Team {
    @JsonProperty("country")
    Country country;
    @JsonProperty("id")
    Long id;
    @JsonProperty("name")
    String name;
    @JsonProperty("nameCode")
    String nameCode;
    @JsonProperty("shortName")
    String shortName;
    @JsonProperty("slug")
    String slug;

}
