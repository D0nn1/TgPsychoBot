package com.example.tgpsychobot.Telegram.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableScheduling;


@EnableScheduling
@Configuration
@Getter
@Setter
@PropertySource("classpath:application.properties")
public class BotConfig {

    private final String botName;
    private final String botToken;
    private final long botOwnerId;

    public BotConfig(@Value("${bot.name}") String botName,
                     @Value("${bot.token}") String botToken,
                     @Value("${bot.ownerId}") long botOwnerId) {
        this.botName = botName;
        this.botToken = botToken;
        this.botOwnerId = botOwnerId;
    }
}
