package ru.home.gr.soccer.bot.service;

import lombok.extern.log4j.Log4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import ru.home.gr.soccer.bot.dictionary.Tournament;
import ru.home.gr.soccer.parse.model.Event;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ru.home.gr.soccer.bot.dictionary.Tournament.UEFA_CHAMPIONS_LEAGUE;
import static ru.home.gr.soccer.bot.dictionary.Tournament.UEFA_EUROPA_LEAGUE;

@Component
@Log4j
public class MatchesProcessorImpl implements MatchesProcessor {

    public static final String EUROPE_MOSCOW = "Europe/Moscow";
    public static final int AMOUNT_TO_ADD = 12;

    @Override
    public List<Event> getMatches(FootballEvents jsonStr, String tournament, boolean yesterday) {
        //Нет смысла искать матчи ЛЧ, ЛЧ только по вторникам и средам!
        if (tournament.equals(UEFA_CHAMPIONS_LEAGUE.getSlug()) && !LocalDate.now()
                                                                            .getDayOfWeek()
                                                                            .equals(DayOfWeek.TUESDAY) && !LocalDate.now()
                                                                                                                    .getDayOfWeek()
                                                                                                                    .equals(DayOfWeek.WEDNESDAY)) {
            return Collections.emptyList();
        }
        //Нет смысла искать матчи ЛЕ, ЛЕ только по четвергам!
        if (tournament.equals(UEFA_EUROPA_LEAGUE.getSlug()) && !LocalDate.now()
                                                                         .getDayOfWeek()
                                                                         .equals(DayOfWeek.THURSDAY)) {
            return Collections.emptyList();
        }

        return jsonStr.getEvents()
                      .stream()
                      .filter(event -> event.getTournament().getUniqueTournament() != null)
                      .filter(event -> event.getTournament().getUniqueTournament().getSlug().equals(tournament))
                      .filter(event -> event.getTournament()
                                            .getCategory()
                                            .getSlug()
                                            .equals(Tournament.getTournamentBySlug(tournament).getTournamentCategory()))
                      .filter(yesterday ? getEventPredicateIsAfterMinusOne() : getEventPredicateIsAfter())
                      .filter(yesterday ? getEventPredicateIsBeforeMinusOne() : getEventPredicateIsBefore())
                      .collect(Collectors.toList());
    }

    @NotNull
    private Predicate<Event> getEventPredicateIsBefore() {
        //до завтрашнего обеда
        return event -> LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.of(EUROPE_MOSCOW))
                                     .isBefore(LocalDateTime.of(LocalDate.now(ZoneId.of(EUROPE_MOSCOW))
                                                                         .plus(1, ChronoUnit.DAYS), LocalTime.NOON.minusMinutes(1)));
    }

    @NotNull
    private Predicate<Event> getEventPredicateIsAfter() {
        //с обеда
        return event -> LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.of(EUROPE_MOSCOW))
                                     .isAfter(LocalDateTime.of(LocalDate.now(ZoneId.of(EUROPE_MOSCOW)), LocalTime.NOON));
        //                                             .plus(AMOUNT_TO_ADD, ChronoUnit.HOURS)));

    }

    @NotNull
    private Predicate<Event> getEventPredicateIsBeforeMinusOne() {
        //до завтрашнего обеда
        return event -> LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.of(EUROPE_MOSCOW))
                                     .isBefore(LocalDateTime.of(LocalDate.now(ZoneId.of(EUROPE_MOSCOW)), LocalTime.NOON.minusMinutes(1)));
    }

    @NotNull
    private Predicate<Event> getEventPredicateIsAfterMinusOne() {
        //с обеда
        return event -> LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.of(EUROPE_MOSCOW))
                                     .isAfter(LocalDateTime.of(LocalDate.now(ZoneId.of(EUROPE_MOSCOW)).minus(1, ChronoUnit.DAYS), LocalTime.NOON));
    }

    @Override
    public Event getMatch(FootballEvents jsonStr, Integer tournamentId, String command) {
        var eventToDisplay = jsonStr.getEvents()
                                    .stream()
                                    .filter(event -> event.getTournament().getId().equals(tournamentId))
                                    .filter(event -> event.getSlug().contains(command))
                                    .filter(getEventPredicateIsAfter())
                                    .filter(getEventPredicateIsBefore())
                                    .findFirst()
                                    .orElse(null);
        if (eventToDisplay != null) {
            eventToDisplay.setMostInterest(true);
        }
        return eventToDisplay;
    }
}
