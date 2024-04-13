package com.example.tgpsychobot.service;

import com.example.tgpsychobot.TelegramBot;
import com.example.tgpsychobot.entity.User;
import com.example.tgpsychobot.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramBotService {

    private final UserRepository repository;

    private TelegramBot bot;

    public TelegramBotService(UserRepository repository) {
        this.repository = repository;
    }

    public TelegramBotService setBot(TelegramBot bot) {
        this.bot = bot;
        return this;
    }

    public TelegramBotService SetMyCommands(TelegramLongPollingBot telegramLongPollingBot) {
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Get a welcome message"));
        listOfCommands.add(new BotCommand("/mydata", "Get your data"));
        listOfCommands.add(new BotCommand("/help", "Get help"));
        listOfCommands.add(new BotCommand("/deletedata", "Delete your data"));
        listOfCommands.add(new BotCommand("/settings", "Set your preferences"));
        try {
            telegramLongPollingBot.execute(new SetMyCommands(listOfCommands,
                    new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occured: " + e.getMessage());
            e.printStackTrace();
        }
        return this;
    }

    public void sendMsg(Message message,
                        String messageText, List<KeyboardRow> rows) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(messageText);

        sendMessage.setReplyMarkup(getReplyKeyboardMarkup(rows));
        try {
            bot.execute(sendMessage);
        } catch (TelegramApiException e) {
            log.error("Error occured: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private ReplyKeyboardMarkup getReplyKeyboardMarkup(List<KeyboardRow> rows) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setKeyboard(rows);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);
        return replyKeyboardMarkup;
    }

    public void registerUser(Message msg) {
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
}
