package com.example.tgpsychobot.Telegram.repository;


import com.example.tgpsychobot.Telegram.model.Ads;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdsRepository extends JpaRepository<Ads, Long> {

}
