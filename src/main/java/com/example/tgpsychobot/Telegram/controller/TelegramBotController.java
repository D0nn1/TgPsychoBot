package com.example.tgpsychobot.Telegram.controller;


import com.example.tgpsychobot.Telegram.config.BotConfig;
import com.example.tgpsychobot.Telegram.model.Ads;
import com.example.tgpsychobot.Telegram.model.MusicTrack;
import com.example.tgpsychobot.Telegram.model.User;
import com.example.tgpsychobot.Telegram.service.AdsService;
import com.example.tgpsychobot.Telegram.service.MusicTrackService;
import com.example.tgpsychobot.Telegram.service.TelegramBotService;
import com.example.tgpsychobot.Telegram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendAudio;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.LinkedList;
import java.util.List;


@Slf4j
@Component
public class TelegramBotController extends TelegramLongPollingBot {

    private final BotConfig config;
    private final TelegramBotService service;
    private final AdsService adsService;
    private final UserService userService;
    private final MusicTrackService musicService;
    private static final Long ID_STORAGE_CHAT = -4177109975L;


    public TelegramBotController(BotConfig config, TelegramBotService tgBotService, AdsService adsService,
                                 UserService userService, MusicTrackService musicService) {
        this.userService = userService;
        this.adsService = adsService;
        this.config = config;
        this.service = tgBotService;
        this.musicService = musicService;
    }


    @Override
    public void onUpdateReceived(Update update) {
        SendMessage sendMessage;
        Message message;
        if (update.hasMessage()) {
            message = update.getMessage();
            System.out.println(message.hasAudio());
            if (message.getChatId().equals(ID_STORAGE_CHAT)) {
                musicService.saveMusicTrack(new MusicTrack(message));
            }
            else if (message.hasText()) {
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
                        messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(),
                                "/sound"));
                    }
                    default -> {
                        messagesForExecute.addAll(service.getMessagesForExecute(update.getMessage(),
                                "not supported"));
                    }
                }

                for (PartialBotApiMethod msg : messagesForExecute) {
                    try {
                        if (msg instanceof SendMessage) {
                            execute((SendMessage) msg);
                            messagesForExecute.remove(msg);
                        } else if (msg instanceof SendAudio) {
                            execute((SendAudio) msg);
                            messagesForExecute.remove(msg);
                        } else if (msg instanceof EditMessageMedia) {
                            execute((EditMessageMedia) msg);
                            messagesForExecute.remove(msg);

                        }
                    } catch (TelegramApiException e) {
                        log.error("Error occured: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        } else if (update.hasCallbackQuery()) {
            String data = update.getCallbackQuery().getData();
            EditMessageText editMessage;
            editMessage = service.getEditMessageFor(update.getCallbackQuery().getMessage(),
                    data);
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
