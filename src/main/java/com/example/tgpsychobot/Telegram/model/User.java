package com.example.tgpsychobot.Telegram.model;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.*;


@Getter
@Setter
@NoArgsConstructor
@Entity(name = "usersDataTable")
public class User {

    @Id
    private Long chatId;

    private String firstName;
    private String lastName;
    private String userName;
    private Timestamp registeredAt;
    private long votes;

    @ElementCollection
    @CollectionTable(name = "liked_tracks", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "track_id")
    private Set<Long> likedTracks = new HashSet<>();

}
