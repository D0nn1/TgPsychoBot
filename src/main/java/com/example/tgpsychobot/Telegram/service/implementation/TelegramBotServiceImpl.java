package com.example.tgpsychobot.Telegram.service.implementation;

import com.example.tgpsychobot.Telegram.model.User;
import com.example.tgpsychobot.Telegram.repository.UserRepository;
import com.example.tgpsychobot.Telegram.service.TelegramBotService;
import com.example.tgpsychobot.Telegram.service.UserService;
import com.example.tgpsychobot.Telegram.util.AnswersForCommands;
import com.example.tgpsychobot.Telegram.util.KeyboardRowsForPsychoBot;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.core5.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class TelegramBotServiceImpl implements TelegramBotService {

    private final UserRepository repository;

    private final UserService userService;

    public TelegramBotServiceImpl(UserRepository repository, UserService userService) {
        this.userService = userService;
        this.repository = repository;
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
        userService.saveUser(msg);
    }

    public EditMessageText getEditMessageFor(Message message, String typeOfCommand) {
        EditMessageText editMessage = new EditMessageText();
        editMessage.setChatId(String.valueOf(message.getChatId()));
        editMessage.setMessageId(message.getMessageId());
        switch (typeOfCommand) {
            case "REGISTER_YES_BUTTON" -> editMessage.setText("Нажата кнопка да");
            case "REGISTER_NO_BUTTON" -> editMessage.setText("Нажата кнопка нет");
            case "UNKNOWN" -> editMessage.setText("Непредвиденная ситуация, обратитесь в поддержку");
        }
        return editMessage;
    }

    @Override
    public List<PartialBotApiMethod> getMessagesForExecute(Message message, String typeOfCommand) {
        typeOfCommand = typeOfCommand.isEmpty() ? "UNKNOWN" : typeOfCommand;
        List<PartialBotApiMethod> messagesForExecute = new LinkedList<>();
        SendMessage sendMessage = new SendMessage();
        EditMessageMedia editMessageMedia = new EditMessageMedia();
        SendAudio sendAudio = new SendAudio();

        sendMessage.setChatId(message.getChatId());
        editMessageMedia.setChatId(message.getChatId());
        sendAudio.setChatId(message.getChatId());

        switch (typeOfCommand) {
            case "/start" -> {
                sendMessage.setText(AnswersForCommands.START.toString());
                sendMessage.setReplyMarkup(getReplyKeyboardMarkup(KeyboardRowsForPsychoBot.start()));
            }
            case "/help" -> {
                sendMessage.setText(AnswersForCommands.HELP.toString());
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
            case "/sound" -> {
                String audioLink = "https://dl2.soundcloudmp3.org/api/download/eyJpdiI6IjNTZFA4Y1F4dFFrSWtxOWNrWE1ncmc9PSIsInZhbHVlIjoieE9xMFJtTGhYckE4Z3BoNHJCMkZUMG05QWpnMExWUXFCNGVXS0NRQm1wU01pOCtyRVlla2NvT25qbTJkVDU0QzA1emRkMW9CU0NNK2FBYWlrenl4XC9NVDF1RzdCbW9JVHVLeEpxcW1vVVwvR2dhSjd1bXN1TUwxU29leFFRYVQ3byIsIm1hYyI6IjQ4NGUyZGRiOWFlZjc5YWQ5YjMxN2UxMTI5ZTJmMTk4MmU1YmI0YjU4NDFkOTIzNzZjMzkyOGMxM2NhYmU3NGUifQ==";
                sendMessage.setText("Трек загружается...");
                File audioFile = new File("src/main/resources/audios/SVDFOREVER - SINFUL WORLD.mp3");
                InputFile inputFile = new InputFile(audioFile);

                sendAudio.setAudio(getAudioFromLink(audioLink));
                messagesForExecute.add(sendAudio);
            }
            case "not supported" -> {
                sendMessage.setText("Пока не поддерживается");
            }
        }
        messagesForExecute.add(sendMessage);
        if (editMessageMedia.getMessageId() != null) messagesForExecute.add(editMessageMedia);
        if (sendAudio.getDuration() != null) messagesForExecute.add(sendAudio);
        return messagesForExecute;
    }

    @Override
    public InputFile getAudioFromLink(String link) {
        CloseableHttpClient client = HttpClients.createDefault();
        HttpGet request = new HttpGet(link);
        InputStream buffer = null;

        try (CloseableHttpResponse response = client.execute(request)) {
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                try (InputStream inputStream = entity.getContent()) {
                    // Создаем InputFile из InputStream
                    buffer = inputStream;
                }
            } else {
                // Обработка случая, когда entity пустой
                System.err.println("Пустой ответ");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new InputFile(buffer, "audio.mp3");
    }

}
