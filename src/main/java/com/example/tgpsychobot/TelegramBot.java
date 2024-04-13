package com.example.tgpsychobot;


import com.example.tgpsychobot.config.BotConfig;
import com.example.tgpsychobot.service.TelegramBotService;
import com.example.tgpsychobot.util.AnswersForCommands;
import com.example.tgpsychobot.util.KeyboardRowsForPsychoBot;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final BotConfig config;
    private final TelegramBotService service;

    private static final String HELP_TEXT = "This bot is for your mental health.\n" +
            "For help ask to @drestoonplaya.\n" +
            "Type /mydata to get your data.\n" +
            "Type /deletedata to delete your data.\n";


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
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            switch (messageText) {
                case "/start":
                    service.registerUser(update.getMessage());
                    service.sendMsg(update.getMessage(), AnswersForCommands.START.toString(),
                            KeyboardRowsForPsychoBot.start());
                    break;

                case "/help":
                    service.sendMsg(update.getMessage(), AnswersForCommands.HELP.toString(),
                            KeyboardRowsForPsychoBot.start());
                    break;
                default:
                    service.sendMsg(update.getMessage(), "Пока не поддерживается",
                            KeyboardRowsForPsychoBot.start());
                    break;
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
