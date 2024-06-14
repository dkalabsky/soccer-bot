package ru.home.gr.soccer.bot.service;

import ru.home.gr.soccer.parse.model.Event;
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.util.List;

public interface MatchesProcessor {
    List<Event> getMatches(FootballEvents jsonStr, String tournament, boolean yesterday);

    Event getMatch(FootballEvents jsonStr, Integer tournament, String command);
}
