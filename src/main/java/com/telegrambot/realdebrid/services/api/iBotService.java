package com.telegrambot.realdebrid.services.api;

import com.telegrambot.realdebrid.exceptions.ConnectionException;
import com.telegrambot.realdebrid.services.dtos.Authentication;
import com.telegrambot.realdebrid.services.dtos.Client;
import com.telegrambot.realdebrid.services.dtos.UserDTO;
import org.apache.shiro.session.Session;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

public interface iBotService {

    //public by default
    void saveUpdatesInSession(Update update, Session session);
    void handleResponses(Update update, Session session);
    void handleMagnet(Update update);
    String getPreviousResponse(Session session);
    void login(Update update, Session session) throws TelegramApiException, InterruptedException, IOException, ConnectionException;
    void sendMessageToUser(String messageText, String chatId) throws TelegramApiException;
    UserDTO saveUser(Update update, Authentication authenticationDTO, Client client);
    boolean isLoggedIn(String userId);

}
