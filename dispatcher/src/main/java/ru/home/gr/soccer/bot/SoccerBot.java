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
import ru.home.gr.soccer.bot.service.FootballInfoProcessor;

import java.io.IOException;

@Component
@Log4j
public class SoccerBot extends TelegramLongPollingBot {
    public static final String NOTIFY_MATCHES = "Напоминание о сегодняшних матчах";
    public final BotConfig botConfig;
    public final FootballInfoProcessor footballInfoProcessor;

    public SoccerBot(BotConfig botConfig, FootballInfoProcessor footballInfoProcessor) {
        super(botConfig.getToken());
        this.botConfig = botConfig;
        this.footballInfoProcessor = footballInfoProcessor;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {
    }


    @Scheduled(cron = "${event.cron.scheduler.by.everyday}")
    @Scheduled(fixedDelay = 1000000000)
    public void startEventInfo() {
        StringBuilder matches = new StringBuilder();
        try {
            matches.append(footballInfoProcessor.getFootballInfo(true));
            matches.append("\n");
            matches.append(footballInfoProcessor.getFootballInfo(false));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendAnswer(SendMessage.builder().chatId(botConfig.getChatId()).text(matches.toString()).parseMode("html").build());
    }

    @Scheduled(cron = "${event.cron.scheduler.by.everyday.repeat}")
    public void startRepeatEvent() {
        sendAnswer(SendMessage.builder().chatId(botConfig.getChatId()).text(NOTIFY_MATCHES).build());
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

}
