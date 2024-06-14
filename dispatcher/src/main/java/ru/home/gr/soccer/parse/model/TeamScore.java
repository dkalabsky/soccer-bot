package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class TeamScore {
    @JsonProperty("current")
    int current;
    @JsonProperty("display")
    int display;
    @JsonProperty("period1")
    int period1;
    @JsonProperty("period2")
    int period2;
    @JsonProperty("normaltime")
    int normaltime;

}
