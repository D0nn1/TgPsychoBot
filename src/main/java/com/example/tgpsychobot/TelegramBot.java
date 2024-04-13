package com.example.tgpsychobot;


import com.example.tgpsychobot.config.BotConfig;
import com.example.tgpsychobot.service.TelegramBotService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final TelegramBotService service;

    public TelegramBot(BotConfig config, TelegramBotService tgBotService) {
        this.config = config;
        this.service = tgBotService;
    }

    @PostConstruct
    public void init() {
        service.SetMyCommands(this).setBot(this);
    }


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/start" -> {
                    service.registerUser(update.getMessage());
                    sendMessage = service.getSendMessageFor(update.getMessage(), "/start");
                }
                case "/help" -> {
                    sendMessage = service.getSendMessageFor(update.getMessage(), "/help");
                }
                case "/register" -> {
                    sendMessage = service.getSendMessageFor(update.getMessage(), "/register");
                }
                default -> {
                    sendMessage = service.getSendMessageFor(update.getMessage(), "not supported");
                }
            }
            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                log.error("Error occured: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            EditMessageText editMessage;
            if (data.equals("REGISTER_YES_BUTTON")) {
                editMessage = service.getEditMessageFor(update.getCallbackQuery().getMessage(), "REGISTER_YES_BUTTON");
            } else if (data.equals("REGISTER_NO_BUTTON")) {
                editMessage = service.getEditMessageFor(update.getCallbackQuery().getMessage(), "REGISTER_NO_BUTTON");
            } else {
                editMessage = service.getEditMessageFor(update.getCallbackQuery().getMessage(), "UNKNOWN");
            }
            try {
                execute(editMessage);
            } catch (TelegramApiException e) {
                log.error("Error occured: " + e.getMessage());
                e.printStackTrace();
            }
        }

    }


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

}
