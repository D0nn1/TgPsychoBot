package com.example.tgpsychobot.Telegram.service.implementation;

import com.example.tgpsychobot.Telegram.model.Ads;
import com.example.tgpsychobot.Telegram.repository.AdsRepository;
import com.example.tgpsychobot.Telegram.service.AdsService;
import org.springframework.stereotype.Service;

@Service
public class AdsServiceImpl implements AdsService {

    private final AdsRepository repository;

    public AdsServiceImpl(AdsRepository repository) {
        this.repository = repository;
    }


    @Override
    public Iterable<Ads> findAll() {
        return repository.findAll();
    }
}


