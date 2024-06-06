package ru.home.gr.soccer.bot.service;

import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import ru.home.gr.soccer.bot.dictionary.Tournament;
import ru.home.gr.soccer.parse.RequestBot;
import ru.home.gr.soccer.parse.model.Event;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static java.time.Instant.ofEpochSecond;
import static java.time.LocalDateTime.ofInstant;
import static ru.home.gr.soccer.bot.dictionary.Tournament.getTournamentBySlug;

@Service
@Log4j
public class FootballInfoProcessor {

    public static final String EUROPE_MOSCOW = "Europe/Moscow";
    public final MatchesProcessor matchesProcessor;

    public FootballInfoProcessor(MatchesProcessor matchesProcessor) {
        this.matchesProcessor = matchesProcessor;
    }

    public StringBuilder getFootballInfo() throws IOException {
        var jsonStr = RequestBot.builder().build().tryToGetFromWebBody();
        if (Objects.isNull(jsonStr)) {
            throw new RuntimeException("Сервис временно не доступен!");
        }

        Set<Event> allEvents = getAllEvents(jsonStr);
        if (allEvents.isEmpty()) {
            return new StringBuilder();
        }

        return buildMatchInfo(allEvents);
    }

    @NotNull
    private Set<Event> getAllEvents(FootballEvents jsonStr) {
        //либо лч, чм, че и тд
        Set<Event> allEvents = new HashSet<>();
        Tournament.getAllTournaments().forEach(t -> allEvents.addAll(matchesProcessor.getMatches(jsonStr, t)));
        allEvents.remove(null);
        return allEvents;
    }

    @NotNull
    private StringBuilder buildMatchInfo(Set<Event> allEvents) {
        StringBuilder matchesForDisplay = new StringBuilder();
        List<String> usedData = new ArrayList<>();
        allEvents.stream()
                 .sorted(Comparator.comparing(Event::getTournament).thenComparing(Event::getStartTimestamp))
                 .forEach(event -> fillMatches(usedData, matchesForDisplay, event));
        log.info("Result was send to telegram!");
        return matchesForDisplay;
    }

    private void fillMatches(List<String> usedData, StringBuilder matchesForDisplay, Event event) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM HH:mm");
        var startTime = "\n" + ofInstant(ofEpochSecond(event.getStartTimestamp()), ZoneId.of(EUROPE_MOSCOW)).format(formatter) + " ";
        var tournamentDescription = getTournamentBySlug(event.getTournament()
                                                             .getUniqueTournament()
                                                             .getSlug()).getDescriptionShortRu();
        var tournamentName = usedData.contains("\n\n" + tournamentDescription) || usedData.contains("\n" + tournamentDescription) ? "" : "\n\n" + tournamentDescription;

        if (matchesForDisplay.isEmpty() && !tournamentName.isEmpty()) {
            tournamentName = tournamentName.replace("\n\n", "\n");
        }

        matchesForDisplay.append(tournamentName)
                         .append(startTime)
                         .append(event.getHomeTeam().getName())
                         .append(" : ")
                         .append(event.getAwayTeam().getName());
        usedData.add(tournamentName);
    }

    @NotNull
    private Set<Event> getMostEvents(FootballEvents jsonStr) {
        //все игры на текущий день(тут понять про правильность времени и зон)
        //найти игры МЮ и ЦСКА, сборная РФ
        //либо лига
        Set<Event> allEvents = new HashSet<>();
        Tournament.getCskaMuNationalTeam()
                  .forEach((key, value) -> allEvents.add(matchesProcessor.getMatch(jsonStr, key, value)));
        allEvents.remove(null);
        return allEvents;
    }

    @Deprecated(forRemoval = true)
    private void fillMatchesOld(List<String> usedDates, StringBuilder matchesForDisplay, Event champion) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        var startTime = ofInstant(ofEpochSecond(champion.getStartTimestamp()), ZoneId.of(EUROPE_MOSCOW)).format(formatter);
        String startTimeString = usedDates.contains(startTime) ? "" : "\n " + startTime + " \n";
        matchesForDisplay.append(startTimeString)
                         .append("<b>")
                         .append(champion.getHomeTeam().getName())
                         .append("</b>")
                         .append(" : ")
                         .append("<i>")
                         .append(champion.getAwayTeam().getName())
                         .append("</i> (")
                         .append(getTournamentBySlug(champion.getTournament()
                                                             .getUniqueTournament()
                                                             .getSlug()).getDescriptionShortRu())
                         .append(") \n");
        usedDates.add(startTime);
    }

}
