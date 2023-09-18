package ru.home.gr.soccer.bot.dictionary;

import lombok.Getter;

import java.util.Arrays;

@Getter
public enum Tournament {

    EPL(1, "premier-league","England premiere league", "АПЛ"),
    RPL(53, "premier-liga","Russian premiere league", "РПЛ"),
    WEPL(15635, "premier-league-women", "Woman England premiere league", "АПЛ(Ж)"),
    WRPL(2345, "premier-league-women", "Woman Russian premiere league", "РПЛ(Ж)"),
    UEFA_CHAMPIONS_LEAGUE(1466, "uefa-champions-league", "UEFA Champions League", "Лига Чемпионов"),
    UEFA_EURO(4508, "european-championship-qualification", "UEFA European Championship", "Чемпионат Европы"),
    UEFA_WORLD_CUP(-2, "word-championship-qualification", "UEFA World Cup", "Чемпионат Мира"),
    WORLD_FRIENDLY(66, "int-friendly-games", "Int. Friendly Games", "Товарищеский матч(сборные)"),
    CONMEBOL(1434, "world-championship-qual-conmebol", "World Championship Qual. CONMEBOL", "ЧМ Южная Америка"),
    RUSSIAN_CUP(87665, "russian-cup", "Russian Cup", "Кубок России"),
    ENGLAND_CUP(-1, "england-cup", "England Cup", "Кубок Англии"),
    LA_LIGA(36, "laliga", "LaLiga", "Ла Лига"),
    SPANISH_CUP(-2, "spanish-cup", "Spanish Cup", "Кубок Испании"),
    SERIE_A(33, "serie-a", "Serie A", "Сериа А"),
    ITALY_CUP(-3, "italy-cup", "Italy Cup", "кубок Италии"),
    NA(0, "tournament not found", "tournament not found", "Такого турнира нет в списке");


    private final Integer id;
    private final String slug;
    private final String description;
    private final String descriptionShortRu;

    Tournament(int id, String slug, String description, String descriptionShortRu) {
        this.id = id;
        this.slug = slug;
        this.description = description;
        this.descriptionShortRu = descriptionShortRu;
    }

    public static Tournament getTournamentDescriptionRuBySlug(String slug){
        return Arrays.stream(Tournament.values()).filter(t->t.getSlug().equals(slug)).findFirst().orElse(NA);
    }
}
