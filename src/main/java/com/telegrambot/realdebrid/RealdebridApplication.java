package com.telegrambot.realdebrid;

import com.telegrambot.realdebrid.bot.RealDebridBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@SpringBootApplication
public class RealdebridApplication implements CommandLineRunner {


	@Autowired
	public void setRealDebridBotRealDebridBot(RealDebridBot realDebridBot) {
		this.realDebridBot = realDebridBot;
	}
	private RealDebridBot realDebridBot;

	public static void main(String[] args) {
			SpringApplication.run(RealdebridApplication.class, args);
	}

	//We can't use an instance variable in a static method, so use CommandLineRunner and add implementation to run method
	@Override
	public void run(String... args) throws Exception {
		TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
		telegramBotsApi.registerBot(realDebridBot);
	}
}
