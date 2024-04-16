package com.example.tgpsychobot.Telegram.service.implementation;


import com.example.tgpsychobot.Telegram.model.User;
import com.example.tgpsychobot.Telegram.repository.UserRepository;
import com.example.tgpsychobot.Telegram.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.sql.Timestamp;

@Slf4j
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

    @Override
    public boolean saveUser(Message msg) {
        if (repository.findById(msg.getChatId()).isEmpty()) {
            User user = new User();
            user.setChatId(msg.getChatId());
            user.setFirstName(msg.getChat().getFirstName());
            user.setLastName(msg.getChat().getLastName());
            user.setUserName(msg.getChat().getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            repository.save(user);
            log.info("User \"{}\" registered", msg.getChat().getFirstName());
            return true;
        }
        return false;
    }
}
