package com.example.tgpsychobot.Telegram.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.telegram.telegrambots.meta.api.objects.Message;

import java.util.Objects;

@NoArgsConstructor
@Slf4j
@Entity(name = "musicTracks")
@Getter
@Setter
public class MusicTrack {
    @Id
    private Long id;
    private long rating;
    private long approves;
    private long denies;
    private String name;


    public MusicTrack(Message message) {
        if (message.hasAudio()) {
            this.name = message.getAudio().getFileName();
            this.id = (long) message.getMessageId();
        } else {
            log.error("Message " + message.getMessageId() + " has no audio");
            throw new IllegalArgumentException("Message has no audio");
        }
    }

    public void incrementApprove() {
        approves++;
        rating = approves - denies;
    }

    public void incrementDeny() {
        denies++;
        rating = approves - denies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MusicTrack that = (MusicTrack) o;
        return id.equals(that.id); // Сравниваем по ID, предполагая его уникальность
    }

    @Override
    public int hashCode() {
        return Objects.hash(id); // Генерируем hashCode на основе ID
    }
}
