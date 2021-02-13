package com.telegrambot.realdebrid.bot;

import com.telegrambot.realdebrid.services.api.iBotService;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.session.TelegramLongPollingSessionBot;

import java.util.Optional;

import static com.telegrambot.realdebrid.common.CommonConstants.*;

@Component
public class RealDebridBot extends TelegramLongPollingSessionBot {

    private Logger logger = LoggerFactory.getLogger(RealDebridBot.class);

    private iBotService botService;

    @Autowired
    public void setBotService(iBotService botService) {
        this.botService = botService;
    }

    @Value("${realdebrid.username}")
    private String botName;

    @Value("${realdebrid.api}")
    private String botApi;

    @Override
    public void onUpdateReceived(Update update, Optional<Session> optionalSession) {
        //Run each request in a different thread.
        new Thread(() -> {
            logger.info("Starting a new thread:  {}", Thread.currentThread().getName());
            //Let's send a proper message to the user if something goes wrong, we don't want them waiting for their entire life if something goes  wrong
            Session session = null;
            if (optionalSession.isPresent())
                session = optionalSession.get();

            //Let's try to remember the chat history for a particular session
            botService.saveUpdatesInSession(update, session);
            try {
                String text = update.getMessage().getText();
                logger.info("Text recieved: {}", text);
                switch (text) {
                    case OPTION_LOGIN:
                        logger.info("Login Request");
                        botService.login(update, session);
                        break;
                    case OPTION_MAGNET:
                        logger.info("It's a Magnet");
                        if(botService.isLoggedIn(update.getMessage().getFrom().getId().toString())){
                            logger.info("User has already logged in");
                            botService.sendMessageToUser(MESSAGE_ENTER_MAGNET_URL, update.getMessage().getChatId().toString());
                        } else {
                            logger.info("User not logged in");
                            botService.sendMessageToUser("Please login to real debrid before performing this action", update.getMessage().getChatId().toString());
                            botService.login(update, session);
                        }
                        break;
                    default:
                        logger.info("Something else, handle response takes care of this");
                        botService.handleResponses(update, session);
                }
            } catch (Exception e) {
                logger.error(e.getMessage());
                try {
                    botService.sendMessageToUser("Sorry, Something went wrong", update.getMessage().getChatId().toString());
                } catch (TelegramApiException ea) {
                    logger.error(ea.getMessage());
                }
            }
        }).start();
        // The thread will be destroyed once whatever is there within the run method is completed
    }

    public String getBotUsername() {
        if(StringUtils.isEmpty(botName)) {
            logger.error("Please goto src -> java -> resources -> application.properties and verify that a valid realdebrid.username is provided");
        }
        return botName;
    }

    public String getBotToken() {
        if(StringUtils.isEmpty(botApi)) {
            logger.error("Please goto src -> java -> resources -> application.properties and verify that a valid realdebrid.api is provided");
        }
        return botApi;
    }
}
