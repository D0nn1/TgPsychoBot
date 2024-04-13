package com.example.tgpsychobot.service;


import com.example.tgpsychobot.TelegramBot;
import com.example.tgpsychobot.service.implementation.TelegramBotServiceImpl;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.List;
public interface TelegramBotService {
    TelegramBotServiceImpl setBot(TelegramBot bot);

    TelegramBotServiceImpl SetMyCommands(TelegramLongPollingBot telegramLongPollingBot);

    void registerUser(Message message);

    SendMessage getSendMessageFor(Message message, String typeOfComand);

    public EditMessageText getEditMessageFor(Message message, String typeOfComand);


}
