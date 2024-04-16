package com.example.tgpsychobot.Telegram.service;


import com.example.tgpsychobot.Telegram.service.implementation.TelegramBotServiceImpl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

public interface TelegramBotService {
    TelegramBotServiceImpl SetMyCommands(TelegramLongPollingBot telegramLongPollingBot);

    void registerUser(Message message);

    EditMessageText getEditMessageFor(Message message, String typeOfComand);

    List<PartialBotApiMethod> getMessagesForExecute(Message message, String typeOfComand);

    InputFile getAudioFromLink(String link);

}
