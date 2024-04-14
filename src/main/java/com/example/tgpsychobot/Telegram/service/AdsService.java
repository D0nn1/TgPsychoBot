package com.example.tgpsychobot.Telegram.service;

import com.example.tgpsychobot.Telegram.model.Ads;

public interface AdsService {
    Iterable<Ads> findAll();
}
