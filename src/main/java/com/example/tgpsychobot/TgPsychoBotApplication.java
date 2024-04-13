package com.example.tgpsychobot;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class TgPsychoBotApplication {

	public static void main(String[] args) {
		log.info("\n ################### \n TgPsychoBot started \n ###################" );
		SpringApplication.run(TgPsychoBotApplication.class, args);
	}

}
