package com.example.tgpsychobot.Telegram.repository;

import com.example.tgpsychobot.Telegram.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
