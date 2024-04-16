package com.example.tgpsychobot.Telegram.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


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
    private HashSet<Long> likedTracks = new HashSet<>();

}
