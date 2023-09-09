package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
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
    @JsonProperty("tournament")
    Tournament tournament;
    @JsonProperty("homeTeam")
    Team homeTeam;
    @JsonProperty("awayTeam")
    Team awayTeam;
    //заполняется true для сортировки интересующих команд
    boolean mostInterst;
}
