package ru.home.gr.soccer.bot;

import lombok.extern.log4j.Log4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.botapimethods.BotApiMethodMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.home.gr.soccer.bot.config.BotConfig;

@Component
@Log4j
public class SoccerBot extends TelegramLongPollingBot {
    public final BotConfig botConfig;

    public SoccerBot(BotConfig botConfig) {
        this.botConfig = botConfig;
    }

    @Override
    public String getBotUsername() {
        return botConfig.getBotName();
    }

    @Override
    public void onUpdateReceived(Update update) {


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

}
