package com.example.tgpsychobot.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class BotConfig {

    private final String botName;
    private final String botToken;

    public BotConfig(@Value("${bot.name}") String botName, @Value("${bot.token}") String botToken) {
        this.botName = botName;
        this.botToken = botToken;
    }
}
