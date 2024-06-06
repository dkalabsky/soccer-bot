package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class EventStatus {
    @JsonProperty("code")
    Long code;
    @JsonProperty("description")
    String description;
    @JsonProperty("type")
    String type;
}
