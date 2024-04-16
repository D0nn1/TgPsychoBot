package com.example.tgpsychobot.Telegram.repository;

import com.example.tgpsychobot.Telegram.model.MusicTrack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MusicTrackRepository extends JpaRepository<MusicTrack, Long> {
}
