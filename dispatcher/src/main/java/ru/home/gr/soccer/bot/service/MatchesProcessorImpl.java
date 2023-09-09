package ru.home.gr.soccer.bot.service;

import org.springframework.stereotype.Component;
import ru.home.gr.soccer.parse.model.Event;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.home.gr.soccer.bot.dictionary.Tournament.UEFA_CHAMPIONS_LEAGUE;

@Component
public class MatchesProcessorImpl implements MatchesProcessor {

    @Override
    public Event getMatch(FootballEvents jsonStr, Integer tournamentId, String command) {
        var eventToDisplay = jsonStr.getEvents().stream()
                .filter(event -> event.getTournament().getId().equals(tournamentId))
                .filter(event -> event.getSlug().contains(command))
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
        return jsonStr.getEvents().stream()
                .filter(event -> event.getTournament().getUniqueTournament().getSlug().equals(tournament))
                .collect(Collectors.toList());
    }
}
