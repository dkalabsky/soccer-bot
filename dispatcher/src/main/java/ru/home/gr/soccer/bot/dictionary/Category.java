package ru.home.gr.soccer.bot.dictionary;

import lombok.Getter;

@Getter
public enum Category {

    EPL(1, "england", "Англия"),
    RPL(21, "russia", "Россия"),
    EUROPE(1465, "europe", "Европа"),
    WORLD(1468, "world", "Мир"),
    SOUTH_AMERICA(1470, "south-america", "Южная Америка");


    private final Integer id;
    private final String description;
    private final String descriptionRu;

    Category(int id, String description, String descriptionRu) {
        this.id = id;
        this.description = description;
        this.descriptionRu = descriptionRu;
    }
}
