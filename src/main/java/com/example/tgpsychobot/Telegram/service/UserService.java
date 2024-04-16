package com.example.tgpsychobot.Telegram.service;

import com.example.tgpsychobot.Telegram.model.User;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface UserService {
    Iterable<User> findAll();

    boolean saveUser(Message msg);
}
