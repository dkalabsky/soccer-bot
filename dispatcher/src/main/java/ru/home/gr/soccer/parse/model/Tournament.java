package ru.home.gr.soccer.parse.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Tournament implements Comparable<Tournament>{
    @JsonProperty("id")
    Integer id;
    @JsonProperty("category")
    Category category;
    @JsonProperty("uniqueTournament")
    UniqueTournament uniqueTournament;

    @Override
    public int compareTo(@NotNull Tournament tournament) {
        return this.id.compareTo(tournament.getId());
    }
}
