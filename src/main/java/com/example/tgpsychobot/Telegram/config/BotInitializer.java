package com.example.tgpsychobot.Telegram.config;


import com.example.tgpsychobot.Telegram.controller.TelegramBotController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


@Slf4j
@Component
public class BotInitializer {

    private final TelegramBotController telegramBot;

    public BotInitializer(TelegramBotController telegramBot) {
        this.telegramBot = telegramBot;
    }

    @EventListener({ContextRefreshedEvent.class})
    public void init() throws TelegramApiException {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(telegramBot);
        } catch (TelegramApiException e) {
            log.error("Error occured: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
