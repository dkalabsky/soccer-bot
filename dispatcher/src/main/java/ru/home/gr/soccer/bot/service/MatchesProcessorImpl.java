package ru.home.gr.soccer.bot.service;

import lombok.extern.log4j.Log4j;
import org.springframework.stereotype.Component;
import ru.home.gr.soccer.bot.dictionary.Category;
import ru.home.gr.soccer.bot.dictionary.Tournament;
import ru.home.gr.soccer.parse.model.Event;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.home.gr.soccer.bot.dictionary.Tournament.UEFA_CHAMPIONS_LEAGUE;
import static ru.home.gr.soccer.bot.dictionary.Tournament.UEFA_EUROPA_LEAGUE;

@Component
@Log4j
public class MatchesProcessorImpl implements MatchesProcessor {

    @Override
    public Event getMatch(FootballEvents jsonStr, Integer tournamentId, String command) {
        var eventToDisplay = jsonStr.getEvents().stream()
                .filter(event -> event.getTournament().getId().equals(tournamentId))
                .filter(event -> event.getSlug().contains(command))
                .filter(event->
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.systemDefault()).isAfter(LocalDateTime.now()))
                .filter(event->
                        LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.systemDefault()).isBefore(LocalDateTime.now().plusDays(1)))
                .findFirst().orElse(null);
        if (eventToDisplay != null) {
            eventToDisplay.setMostInterst(true);
        }
        return eventToDisplay;
    }

    @Override
    public List<Event> getMatches(FootballEvents jsonStr, String tournament) {
        //найти все игры ЛЧ только по вторникам и средам!
        if (tournament.equals(UEFA_CHAMPIONS_LEAGUE.getSlug()) && !LocalDate.now().getDayOfWeek().equals(DayOfWeek.TUESDAY) && !LocalDate.now().getDayOfWeek().equals(DayOfWeek.WEDNESDAY)) {
            return Collections.emptyList();
        }
        //найти все игры ЛЕ только по четвергам!
        if (tournament.equals(UEFA_EUROPA_LEAGUE.getSlug()) && !LocalDate.now().getDayOfWeek().equals(DayOfWeek.THURSDAY)) {
            return Collections.emptyList();
        }
        //TODO временное решение, поменять нормально по возможности
        if(Tournament.EPL.getSlug().equals(tournament)) {
            return jsonStr.getEvents().stream()
                    .filter(event -> event.getTournament().getUniqueTournament() != null)
                    .filter(event -> event.getTournament().getUniqueTournament().getSlug().equals(tournament))
                    .filter(event -> event.getTournament().getCategory().getSlug().equals(Category.ENGLAND.getDescription()))
                    .filter(event ->
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.systemDefault()).isAfter(LocalDateTime.now()))
                    .filter(event->
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.systemDefault()).isBefore(LocalDateTime.now().plusDays(1)))
                    .collect(Collectors.toList());
        } else {
            return jsonStr.getEvents().stream()
                    .filter(event -> event.getTournament().getUniqueTournament() != null)
                    .filter(event -> event.getTournament().getUniqueTournament().getSlug().equals(tournament))
                    .filter(event ->
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.systemDefault()).isAfter(LocalDateTime.now()))
                    .filter(event->
                            LocalDateTime.ofInstant(Instant.ofEpochSecond(event.getStartTimestamp()), ZoneId.systemDefault()).isBefore(LocalDateTime.now().plusDays(1)))
                    .collect(Collectors.toList());
        }
    }
}
