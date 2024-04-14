package com.example.tgpsychobot.service.implementation;

import com.example.tgpsychobot.TelegramBot;
import com.example.tgpsychobot.entity.User;
import com.example.tgpsychobot.repository.UserRepository;
import com.example.tgpsychobot.service.TelegramBotService;
import com.example.tgpsychobot.util.AnswersForCommands;
import com.example.tgpsychobot.util.KeyboardRowsForPsychoBot;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class TelegramBotServiceImpl implements TelegramBotService {

    private final UserRepository repository;

    private TelegramBot bot;

    public TelegramBotServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    public TelegramBotServiceImpl setBot(TelegramBot bot) {
        this.bot = bot;
        return this;
    }

    public TelegramBotServiceImpl SetMyCommands(TelegramLongPollingBot telegramLongPollingBot) {
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

    //
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

    public SendMessage getSendMessageFor(Message message, String typeOfComand) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId().toString());
        switch (typeOfComand) {
            case "/start" -> {
                sendMessage.setText(AnswersForCommands.START.toString());
                sendMessage.setReplyMarkup(getReplyKeyboardMarkup(KeyboardRowsForPsychoBot.start()));
            }
            case "/help" -> {
                sendMessage.setText(AnswersForCommands.HELP.toString());
                sendMessage.setReplyMarkup(getReplyKeyboardMarkup(KeyboardRowsForPsychoBot.start()));
            }
            case "/register" -> {
                sendMessage.setText("Ты действительно хочешь зарегистрироваться?");
                InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
                List<List<InlineKeyboardButton>> rows = new ArrayList<>();
                List<InlineKeyboardButton> row = new ArrayList<>();
                InlineKeyboardButton yesButton = new InlineKeyboardButton();
                yesButton.setText("Да");
                yesButton.setCallbackData("REGISTER_YES_BUTTON");

                InlineKeyboardButton noButton = new InlineKeyboardButton();
                noButton.setText("Нет");
                noButton.setCallbackData("REGISTER_NO_BUTTON");

                row.add(yesButton);
                row.add(noButton);

                rows.add(row);
                markup.setKeyboard(rows);
                sendMessage.setReplyMarkup(markup);
            }


            case "not supported" -> {
                sendMessage.setText("Пока не поддерживается");
                sendMessage.setReplyMarkup(getReplyKeyboardMarkup(KeyboardRowsForPsychoBot.start()));
            }
        }
        return sendMessage;
    }

    public EditMessageText getEditMessageFor(Message message, String typeOfComand) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(message.getChatId()));
        editMessage.setMessageId(message.getMessageId());
        if (typeOfComand.equals("REGISTER_YES_BUTTON")) {
            editMessage.setText("Нажата кнопка да");
        } else if (typeOfComand.equals("REGISTER_NO_BUTTON")) {
            editMessage.setText("Нажата кнопка нет");
        } else if (typeOfComand.equals("UNKNOWN")) {
            editMessage.setText("Непредвиденная ситуация, обратитесь в поддержку");
        }
        return editMessage;
    }

}
