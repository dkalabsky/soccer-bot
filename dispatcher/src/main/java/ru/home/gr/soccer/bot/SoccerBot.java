package ru.home.gr.soccer.bot;

import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodBoolean;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.gr.soccer.bot.config.BotConfig;
import ru.home.gr.soccer.bot.dictionary.Team;
import ru.home.gr.soccer.bot.dictionary.Tournament;
import ru.home.gr.soccer.bot.service.MatchesProcessor;
import ru.home.gr.soccer.parse.RequestBot;
import ru.home.gr.soccer.parse.model.Event;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
@Log4j
public class SoccerBot extends TelegramLongPollingBot {
    public final BotConfig botConfig;
    public final MatchesProcessor matchesProcessor;

    public SoccerBot(BotConfig botConfig, MatchesProcessor matchesProcessor) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.matchesProcessor = matchesProcessor;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
        var msg = update.getMessage();

        List<String> matches = null;
        try {
            matches = getFootballInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessage sm = new SendMessage();
        sm.setChatId(msg.getChatId());
        sm.setText(matches == null ? "Сегодня матчей МЮ и ЦСКА нет" : matches.toString());
        sendAnswer(sm);
    }

    private List<String> getFootballInfo() throws IOException {
        RequestBot rq = new RequestBot();
        //все игры на текущий день(тут понять про правильность времени и зон)
        var jsonStr = rq.getFromWebBody();
        //найти игры МЮ и ЦСКА, сборная РФ

//либо лига
        List<String> matchesForDisplay = new ArrayList<>();
        Set<Event> allEvents = new HashSet<>();
        Map.of(Tournament.RPL.getId(), Team.CSKA_MOSCOW.getDescription(),
                Tournament.EPL.getId(), Team.MANCHESTER_UNITED.getDescription(),
                Tournament.RUSSIAN_CUP.getId(), Team.CSKA_MOSCOW.getDescription(),
                Tournament.ENGLAND_CUP.getId(), Team.MANCHESTER_UNITED.getDescription())
                .forEach((key, value) -> allEvents.add(matchesProcessor.getMatch(jsonStr, key, value)));
//либо лч, чм, че и тд
        List.of(Tournament.UEFA_CHAMPIONS_LEAGUE.getSlug(),
                Tournament.UEFA_EURO.getSlug(),
                Tournament.UEFA_WORLD_CUP.getSlug(),
                Tournament.WORLD_FRIENDLY.getSlug(),
                Tournament.CONMEBOL.getSlug(),
                Tournament.RUSSIAN_CUP.getSlug(),
                Tournament.ENGLAND_CUP.getSlug())
                .forEach(t -> allEvents.addAll(matchesProcessor.getMatches(jsonStr, t)));
//        allEvents.entrySet().stream().sorted(Map.Entry.comparingByValue());
        allEvents.remove(null);

        if (!allEvents.isEmpty()) {

            allEvents.stream()
                    .sorted(Comparator.comparing(Event::isMostInterst))
                    .sorted(Comparator.comparing(Event::getStartTimestamp))
                    .forEach(champion -> {
                        var startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(champion.getStartTimestamp()), ZoneId.systemDefault());
                        matchesForDisplay.add("Начало в: " + startTime + " "
                                + champion.getHomeTeam().getName()
                                + " vs "
                                + champion.getAwayTeam().getName()
                                + ".\n ");
                    });
        }


//        dateTimes.forEach(d->lst.add(allEvents.(d)));
        //найти все игры Сборных
        //найти все игры ЛЕ
        //найти все игры ЧМ
        //найти все игры ЧЕ
        //найти все игры Англии
        //найти все игры Испании
        //найти все игры Германии
        //найти все игры Италии
        //найти все игры Франции
        return matchesForDisplay;
    }


    @Scheduled(cron = "${event.cron.scheduler.by.sunday}")
    public void startEventPoll() {

    }

    public void sendAnswer(BotApiMethodMessage message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

    public void sendPinMessage(BotApiMethodBoolean message) {
        if (message != null) {
            try {
                execute(message);
            } catch (TelegramApiException e) {
                log.error(e);
            }
        }
    }

}
