package com.example.tgpsychobot.Telegram.controller;


import com.example.tgpsychobot.Telegram.config.BotConfig;
import com.example.tgpsychobot.Telegram.model.Ads;
import com.example.tgpsychobot.Telegram.model.User;
import com.example.tgpsychobot.Telegram.service.AdsService;
import com.example.tgpsychobot.Telegram.service.TelegramBotService;
import com.example.tgpsychobot.Telegram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.InputFile;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;


@Slf4j
@Component
public class TelegramBotController extends TelegramLongPollingBot {

    private final BotConfig config;
    private final TelegramBotService service;
    private final AdsService adsService;
    private final UserService userService;


    public TelegramBotController(BotConfig config, TelegramBotService tgBotService, AdsService adsService,
                                 UserService userService) {
        this.userService = userService;
        this.adsService = adsService;
        this.config = config;
        this.service = tgBotService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage;
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            List<PartialBotApiMethod> messagesForExecute = new LinkedList<>();
            switch (messageText) {
                case "/start" -> {
                    service.registerUser(update.getMessage());
                    messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(),
                            "/start"));
                }
                case "/help" -> {
                    messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(),
                            "/help"));
                }
                case "/register" -> {
                    messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(),
                            "/register"));
                }
                case "/sound" -> {
                    messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(), "/sound"));
                    messagesForExecute.forEach(System.out::println);
                }
                default -> {
                    messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(),
                            "not supported"));
                }
            }

            for (PartialBotApiMethod message : messagesForExecute) {
                try {
                    if (message instanceof SendMessage) {
                        execute((SendMessage) message);
                        messagesForExecute.remove(message);
                    } else if (message instanceof SendAudio) {
                        execute((SendAudio) message);
                        messagesForExecute.remove(message);
                    } else if (message instanceof EditMessageMedia) {
                        execute((EditMessageMedia) message);
                        messagesForExecute.remove(message);

                    }
                } catch (TelegramApiException e) {
                    log.error("Error occured: " + e.getMessage());
                    e.printStackTrace();
                }
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

    //во столько секунд:минут:часов:дней:месяцев:дней недель будет запуск
    //@Scheduled(cron = "0 * * * * *") - запуск в 0 секнуд каждой минуты
//    @Scheduled(cron = "${cron.scheduler}")
    public void sendAds() {
        Iterable<Ads> ads = adsService.findAll();
        Iterable<User> users = userService.findAll();
        if (ads != null && users != null) {
            ads.forEach(ad -> {
                users.forEach(user -> {
                    try {
                        execute(new SendMessage(String.valueOf(user.getChatId()),
                        (ad.getAd() + "\n" + user.getUserName() + ", Я твою мать ебал.")));
                    } catch (TelegramApiException e) {
                        throw new RuntimeException(e);
                    }
                });
            });
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
