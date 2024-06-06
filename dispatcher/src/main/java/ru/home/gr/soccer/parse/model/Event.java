package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jdk.jfr.Description;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Event {
    @Description("Id деталей")
    @JsonProperty("detailId")
    Long detailId;

    @Description("Id ")
    @JsonProperty("customId")
    String customId;

    @Description("Id события")
    @JsonProperty("id")
    Long id;

    @Description("Например: mexico-uruguay")
    @JsonProperty("slug")
    String slug;

    @Description("Время начала")
    @JsonProperty("startTimestamp")
    Long startTimestamp;

    @Description("Код победителя")
    @JsonProperty("winnerCode")
    Long winnerCode;

    @Description("Турнир")
    @JsonProperty("tournament")
    Tournament tournament;

    @Description("Команда домашняя")
    @JsonProperty("homeTeam")
    Team homeTeam;

    @Description("Команда на выезде")
    @JsonProperty("awayTeam")
    Team awayTeam;

    @Description("заполняется true для сортировки интересующих команд")
    boolean mostInterest;
}
