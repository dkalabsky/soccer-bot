package ru.home.gr.soccer.bot.dictionary;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Getter
public enum Tournament {

    RPL(53, "premier-liga","Russian premiere league", "РПЛ", "russia"),
    EPL(1, "premier-league","England premiere league", "АПЛ", "england"),
    LA_LIGA(36, "laliga", "LaLiga", "Ла Лига", "spain"),
    SERIE_A(33, "serie-a", "Serie A", "Сериа А", "italy"),
    BUNDESLIGA(42, "bundesliga","BUNDESLIGA", "Бундеслига", "germany"),
    LIGUE_1(7, "ligue-1","Ligue 1", "Лига 1", "france"),
    VISHA_LIGA(86, "premier-league","Premier League", "Вища Лига", "ukraine"),
    SERIE_A_BRAZIL(83, "brasileirao-serie-a", "Brasileirão Série A", "Сериа А Бразилии", "brazil"),

    RUSSIAN_CUP(87665, "russian-cup", "Russian Cup", "Кубок России", "russia"),
    ENGLAND_CUP(17, "efl-cup", "EFL Cup", "Кубок Англии", "england"),
    SPANISH_CUP(-2, "spanish-cup", "Spanish Cup", "Кубок Испании", "spain"),
    ITALY_CUP(-3, "italy-cup", "Italy Cup", "кубок Италии", "italy"),
    DFB_POKAL(43, "dfb-pokal","DFB Pokal", "Кубок Германии", "germany"),
    FRANCE_CUP(-7, "france-cup","France Cup", "Кубок Франции", "france"),

    UEFA_CHAMPIONS_LEAGUE(1466, "uefa-champions-league", "UEFA Champions League", "Лига Чемпионов", "europa"),
    UEFA_EUROPA_LEAGUE(10911, "uefa-europa-league", "UEFA Europa League", "Лига европы", "europa"),
    UEFA_EURO_QUALIFICATION(4508, "european-championship-qualification", "UEFA European Championship", "Чемпионат Европы отборочные", "europa"),
    UEFA_EURO(1465, "european-championship", "UEFA European Championship", "Чемпионат Европы", "europe"),
    UEFA_WORLD_CUP(-2, "word-championship-qualification", "UEFA World Cup", "Чемпионат Мира", "europa"),
    WORLD_FRIENDLY(66, "int-friendly-games", "Int. Friendly Games", "Товарищеский матч(сборные)", "world"),
    CONMEBOL(1434, "world-championship-qual-conmebol", "World Championship Qual. CONMEBOL", "ЧМ Южная Америка", "europa"),
    AFC(7238, "world-championship-qual-afc", "World Championship Qual. AFC", "ЧМ Азия", "asia"),



    WEPL(15635, "premier-league-women", "Woman England premiere league", "АПЛ(Ж)", "england"),
    WRPL(2345, "premier-league-women", "Woman Russian premiere league", "РПЛ(Ж)", "russia"),
    NA(0, "tournament not found", "tournament not found", "Такого турнира нет в списке", "no");


    private final Integer id;
    private final String slug;
    private final String description;
    private final String descriptionShortRu;
    private final String tournamentCategory;

    Tournament(int id, String slug, String description, String descriptionShortRu, String tournamentCategory) {
        this.id = id;
        this.slug = slug;
        this.description = description;
        this.descriptionShortRu = descriptionShortRu;
        this.tournamentCategory = tournamentCategory;
    }

    public static Tournament getTournamentBySlug(String slug){
        return Arrays.stream(Tournament.values()).filter(t->t.getSlug().equals(slug)).findFirst().orElse(NA);
    }


    public static List<String> getAllTournaments(){
       return List.of(
                Tournament.RPL.getSlug(),
                Tournament.EPL.getSlug(),
                Tournament.LA_LIGA.getSlug(),
                Tournament.SERIE_A.getSlug(),
                Tournament.BUNDESLIGA.getSlug(),
                Tournament.LIGUE_1.getSlug(),
                Tournament.RUSSIAN_CUP.getSlug(),
                Tournament.ENGLAND_CUP.getSlug(),
                Tournament.SPANISH_CUP.getSlug(),
                Tournament.ITALY_CUP.getSlug(),
                Tournament.DFB_POKAL.getSlug(),
//                Tournament.FRANCE_CUP.getSlug(),
                Tournament.UEFA_CHAMPIONS_LEAGUE.getSlug(),
                Tournament.UEFA_EUROPA_LEAGUE.getSlug(),
                Tournament.UEFA_EURO_QUALIFICATION.getSlug(),
                Tournament.UEFA_EURO.getSlug(),
                Tournament.UEFA_WORLD_CUP.getSlug(),
                Tournament.CONMEBOL.getSlug(),
                Tournament.WORLD_FRIENDLY.getSlug(),
//                Tournament.SERIE_A_BRAZIL.getSlug(),
                Tournament.VISHA_LIGA.getSlug()
//                Tournament.AFC.getSlug()
        );
    }


    public static Map<Integer, String> getCskaMuNationalTeam(){
        return Map.of(Tournament.RPL.getId(), Team.CSKA_MOSCOW.getDescription(),
                Tournament.EPL.getId(), Team.MANCHESTER_UNITED.getDescription(),
                Tournament.RUSSIAN_CUP.getId(), Team.CSKA_MOSCOW.getDescription(),
                Tournament.ENGLAND_CUP.getId(), Team.MANCHESTER_UNITED.getDescription());
    }
}
