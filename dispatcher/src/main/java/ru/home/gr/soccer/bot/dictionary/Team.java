package ru.home.gr.soccer.bot.dictionary;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Team {

    MANCHESTER_UNITED(35, "manchester-united", "Манчестер Юнайтед"),
    CSKA_MOSCOW(2345, "cska-moscow", "ЦСКА");


    private final Integer id;
    private final String description;
    private final String descriptionRu;

    Team(int id, String description, String descriptionRu) {
        this.id = id;
        this.description = description;
        this.descriptionRu = descriptionRu;
    }

    public static Team getTeamDescriptionRuById(Integer id){
        return Arrays.stream(Team.values()).filter(t->t.getId().equals(id)).findFirst().get();
    }

    public static Team getTeamDescriptionRuByDescriptions(String description){
        return Arrays.stream(Team.values()).filter(t->t.getDescription().equals(description)).findFirst().get();
    }
}
