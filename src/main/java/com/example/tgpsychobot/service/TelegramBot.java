package com.example.tgpsychobot.service;


import com.example.tgpsychobot.config.BotConfig;
import com.example.tgpsychobot.entity.User;
import com.example.tgpsychobot.repository.UserRepository;
import com.example.tgpsychobot.util.KeyboardRowUtil.KeyboardRowAuto;
import com.example.tgpsychobot.util.KeyboardRowUtil.KeyboardRowsAuto;
import com.example.tgpsychobot.util.KeyboardRowsForPsychoBot;
import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;


@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    private final UserRepository repository;
    private final BotConfig config;
    private static final String HELP_TEXT = "This bot is for your mental health.\n" +
            "For help ask to @drestoonplaya.\n" +
            "Type /mydata to get your data.\n" +
            "Type /deletedata to delete your data.\n";


    public TelegramBot(BotConfig config, @Autowired UserRepository repository) {
        this.repository = repository;
        this.config = config;
        setMyCommands();
    }


    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    StartCommandRecieved(chatId, update.getMessage().getChat().getFirstName());
                    break;

                case "/help":
                    sendMsg(chatId, HELP_TEXT);
                    break;
                default:
                    sendMsg(chatId, "Пока не поддерживается");
                    break;
            }
        }
    }

    private void StartCommandRecieved(long chatId, String name) {
        StringBuilder answer = new StringBuilder().append("Привет, ").append(name).append("! ")
                .append(EmojiParser.parseToUnicode(":heart:"));
        log.info("User \"{}\" started bot", name);

        sendMsg(chatId, answer.toString());
    }

    private void sendMsg(long chatId, String msg) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(msg);

        sendMessage.setReplyMarkup(getReplyKeyboardMarkup());
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occured: " + e.getMessage());
            e.printStackTrace();
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

    private void setMyCommands() {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "Get your data"));
        listOfCommands.add(new BotCommand("/help", "Get help"));
        listOfCommands.add(new BotCommand("/deletedata", "Delete your data"));
        listOfCommands.add(new BotCommand("/settings", "Set your preferences"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occured: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void registerUser(Message msg) {
        if (repository.findById(msg.getChatId()).isEmpty()) {
            User user = new User();
            user.setChatId(msg.getChatId());
            user.setFirstName(msg.getChat().getFirstName());
            user.setLastName(msg.getChat().getLastName());
            user.setUserName(msg.getChat().getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            repository.save(user);
            log.info("User \"{}\" registered", msg.getChat().getFirstName());
        }
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();

        replyKeyboardMarkup.setKeyboard(KeyboardRowsForPsychoBot.start());
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        return replyKeyboardMarkup;
    }


}
