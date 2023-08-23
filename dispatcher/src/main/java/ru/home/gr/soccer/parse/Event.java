package ru.home.gr.soccer.parse;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {
    @JsonProperty("detailId")
    Long detailId;
    @JsonProperty("customId")
    String customId;
    @JsonProperty("id")
    Long id;
    @JsonProperty("slug")
    String slug;
    @JsonProperty("startTimestamp")
    Long startTimestamp;
    @JsonProperty("winnerCode")
    Long winnerCode;
}
