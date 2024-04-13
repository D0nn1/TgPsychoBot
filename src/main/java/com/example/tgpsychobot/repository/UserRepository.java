package com.example.tgpsychobot.repository;

import com.example.tgpsychobot.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Long> {
}
