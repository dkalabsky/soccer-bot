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
import ru.home.gr.soccer.parse.model.FootballEvents;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import static ru.home.gr.soccer.bot.dictionary.Tournament.getTournamentDescriptionRuBySlug;

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

    }

    private StringBuilder getFootballInfo() throws IOException {
        RequestBot rq = new RequestBot();
        //все игры на текущий день(тут понять про правильность времени и зон)
        FootballEvents jsonStr = rq.tryToGetFromWebBody();
        if(Objects.isNull(jsonStr)){
            jsonStr = rq.tryToGetFromWebBody();
        }
        if(Objects.isNull(jsonStr)){
            throw new RuntimeException("Сервис временно не доступен!");
        }
        //найти игры МЮ и ЦСКА, сборная РФ
        //либо лига
        StringBuilder matchesForDisplay = new StringBuilder();
        Set<Event> allEvents = new HashSet<>();
        FootballEvents finalJsonStr = jsonStr;
        Map.of(Tournament.RPL.getId(), Team.CSKA_MOSCOW.getDescription(),
                Tournament.EPL.getId(), Team.MANCHESTER_UNITED.getDescription(),
                Tournament.RUSSIAN_CUP.getId(), Team.CSKA_MOSCOW.getDescription(),
                Tournament.ENGLAND_CUP.getId(), Team.MANCHESTER_UNITED.getDescription())
                .forEach((key, value) -> allEvents.add(matchesProcessor.getMatch(finalJsonStr, key, value)));

        //либо лч, чм, че и тд
        List.of(
                Tournament.RPL.getSlug(),
                Tournament.EPL.getSlug(),
                Tournament.LA_LIGA.getSlug(),
                Tournament.SERIE_A.getSlug(),
                Tournament.BUNDESLIGA.getSlug(),
//                Tournament.LIGUE_1.getSlug(),
                Tournament.RUSSIAN_CUP.getSlug(),
                Tournament.ENGLAND_CUP.getSlug(),
                Tournament.SPANISH_CUP.getSlug(),
                Tournament.ITALY_CUP.getSlug(),
                Tournament.DFB_POKAL.getSlug(),
//                Tournament.FRANCE_CUP.getSlug(),
                Tournament.UEFA_CHAMPIONS_LEAGUE.getSlug(),
                Tournament.UEFA_EUROPA_LEAGUE.getSlug(),
                Tournament.UEFA_EURO.getSlug(),
                Tournament.UEFA_WORLD_CUP.getSlug(),
                Tournament.CONMEBOL.getSlug(),
                Tournament.WORLD_FRIENDLY.getSlug()
        )
                .forEach(t -> allEvents.addAll(matchesProcessor.getMatches(finalJsonStr, t)));
        //allEvents.entrySet().stream().sorted(Map.Entry.comparingByValue());
        allEvents.remove(null);

        List<String> usedDates = new ArrayList<>();
        if (!allEvents.isEmpty()) {

            allEvents.stream()
                    .sorted(Comparator.comparing(Event::isMostInterst))
                    .sorted(Comparator.comparing(Event::getStartTimestamp))
                    .forEach(champion -> {
                        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
                        var startTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(champion.getStartTimestamp()), ZoneId.systemDefault()).format(formatter);
                        String startTimeString = usedDates.contains(startTime) ? "" : "\n " + startTime + " \n";
                        matchesForDisplay
                                .append(startTimeString)
                                .append("<b>")
                                .append(champion.getHomeTeam().getName())
                                .append("</b>")
                                .append(" : ")
                                .append("<i>")
                                .append(champion.getAwayTeam().getName())
                                .append("</i> (")
                                .append(getTournamentDescriptionRuBySlug(champion.getTournament().getUniqueTournament().getSlug()).getDescriptionShortRu())
                                .append(") \n");
                        usedDates.add(startTime);
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


    @Scheduled(cron = "${event.cron.scheduler.by.everyday}")
//    @Scheduled(fixedDelay = 1000000)
    public void startEventInfo() {
        StringBuilder matches = new StringBuilder();
        try {
            matches = getFootballInfo();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SendMessage sm = new SendMessage();
        sm.enableHtml(true);
        sm.setChatId(botConfig.getChatId());
        sm.setText(matches.toString());
        sendAnswer(sm);
    }

    @Scheduled(cron = "${event.cron.scheduler.by.everyday.repeat}")
    public void startRepeatEvent() {
        SendMessage sm = new SendMessage();
        sm.setChatId(botConfig.getChatId());
        sm.setText("^^^^^^^^^^^^^^");
        sendAnswer(sm);
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
