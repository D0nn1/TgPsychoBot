package com.example.tgpsychobot.Telegram.service;

import com.example.tgpsychobot.Telegram.model.User;

public interface UserService {
    Iterable<User> findAll();
}
