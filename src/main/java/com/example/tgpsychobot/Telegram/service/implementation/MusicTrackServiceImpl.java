package com.example.tgpsychobot.Telegram.service.implementation;

import com.example.tgpsychobot.Telegram.model.MusicTrack;
import com.example.tgpsychobot.Telegram.repository.MusicTrackRepository;
import com.example.tgpsychobot.Telegram.service.MusicTrackService;
import org.springframework.stereotype.Service;

@Service
public class MusicTrackServiceImpl implements MusicTrackService {
    private final MusicTrackRepository repository;


    public MusicTrackServiceImpl(MusicTrackRepository musicTrackRepository) {
        this.repository = musicTrackRepository;
    }

    @Override
    public void saveMusicTrack(MusicTrack musicTrack) {
        System.out.println("musicTrack = " + musicTrack);
        if (repository.findById(musicTrack.getId()).isEmpty()) {
            repository.save(musicTrack);
        }
    }
}
