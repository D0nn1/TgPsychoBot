package com.example.tgpsychobot.Telegram.service.implementation;


import com.example.tgpsychobot.Telegram.service.UserService;
import com.example.tgpsychobot.Telegram.model.User;
import com.example.tgpsychobot.Telegram.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Iterable<User> findAll() {
        return repository.findAll();
    }
}
