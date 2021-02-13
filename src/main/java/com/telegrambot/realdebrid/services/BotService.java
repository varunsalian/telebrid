package com.telegrambot.realdebrid.services;

import com.telegrambot.realdebrid.bot.RealDebridBot;
import com.telegrambot.realdebrid.exceptions.ConnectionException;
import com.telegrambot.realdebrid.services.api.IRealDebridService;
import com.telegrambot.realdebrid.services.api.iBotService;
import com.telegrambot.realdebrid.services.dtos.Authentication;
import com.telegrambot.realdebrid.services.dtos.Client;
import com.telegrambot.realdebrid.services.dtos.UserDTO;
import com.telegrambot.realdebrid.services.repositories.UserRepository;
import org.apache.shiro.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.telegrambot.realdebrid.common.CommonConstants.*;

@Service
public class BotService implements iBotService {

    private Logger logger = LoggerFactory.getLogger(BotService.class);

    @Autowired
    public void setRealDebridService(IRealDebridService realDebridService) {
        this.realDebridService = realDebridService;
    }
    private IRealDebridService realDebridService;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    private UserRepository userRepository;

    @Autowired
    public void setRealDebridBot(RealDebridBot realDebridBot) {
        this.realDebridBot = realDebridBot;
    }
    private RealDebridBot realDebridBot;


    public void saveUpdatesInSession(Update update, Session session) {
        logger.info("Saving update data inside a session:  {}", update);
        if(session != null) {
            if (session.getAttribute(CHAT_HISTORY) instanceof List) {
                logger.info("Existing session");
                ((List<Update>)session.getAttribute(CHAT_HISTORY)).add(update);
            }
            else {
                logger.info("Non existing session");
                List<Update> updates = new ArrayList<>();
                updates.add(update);
                session.setAttribute(CHAT_HISTORY, updates);
            }
        }
    }

    public void handleResponses(Update update, Session session) {
        String previousResponse = getPreviousResponse(session);
        if(previousResponse!=null) {
            switch (previousResponse) {
                //Check if the previous response was magnet
                case OPTION_MAGNET:
                    handleMagnet(update);
                    break;
                default:
                    break;
            }
        }
        //TODO: Maybe give an option to user, whether to add a non instantly available magnet and notify when completed
    }

    public void handleMagnet(Update update) {
        //check if the magnet URL is valid
        String magnetLink = update.getMessage().getText();
        if(magnetLink==null || !magnetLink.startsWith("magnet"))
            return;
        String hash = getHashFromMagnet(magnetLink);
        if( hash != null) {

        }
    }

    private String getHashFromMagnet(String magnet) {
        String hash = null;
        try {
            hash = magnet.substring(20, 60);
        } catch (StringIndexOutOfBoundsException e) {
            logger.error("Invalid magnet + {}", e.getMessage());
        }
        return hash;
    }

    public String getPreviousResponse(Session session) {
        //Get the message from list of updates (size-2), since the current update is also added to the list
        logger.info("Trying to get the previous text");
        String previousText = null;
        if(session != null &&  ((List)session.getAttribute(CHAT_HISTORY)).size()>=2) {
            List updates =  (List)session.getAttribute(CHAT_HISTORY);
            logger.info("Chat history exists");
            previousText = ((Update) updates.get(updates.size()-2)).getMessage().getText();
        }
        logger.info("Previous text value: {}", previousText );
        // Return null if the size is less than 2
        return previousText;
    }

    public void login(Update update, Session session) throws TelegramApiException, InterruptedException, IOException, ConnectionException {
        //Authenticate the user by getting a verification url from Real Debrid
        Authentication authentication = realDebridService.getRDToken();

        if (authentication != null) {
            //Send a message to the user with the verification URL and the code
            String tokenString = USER_INPUT_GOTO + authentication.getVerificationUrl() + USER_INPUT_ENTER_CODE + authentication.getUserCode();
            sendMessageToUser(tokenString, update.getMessage().getChatId().toString());
            if( session != null) {
                session.setAttribute("loginPending", true);
            }
            //Wait for the user to enter the code and submit(5mins max)
            Client client = realDebridService.waitForLogin(authentication);
            if (client != null) {

                //Save the user details to Database
                saveUser(update, authentication, client);

                //Send a message after successful login
                sendMessageToUser("LOGGED IN", update.getMessage().getChatId().toString());
            } else {
                sendMessageToUser("LOGIN FAILED", update.getMessage().getChatId().toString());
            }
        } else {
            sendMessageToUser("LOGIN FAILED", update.getMessage().getChatId().toString());
        }
        if( session != null) {
            session.setAttribute("loginPending", false);
        }
    }

    public void sendMessageToUser(String messageText, String chatId) throws TelegramApiException {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(messageText);
        realDebridBot.execute(sendMessage);
    }

    public UserDTO saveUser(Update update, Authentication authenticationDTO, Client client) {
        User user = update.getMessage().getFrom();
        UserDTO userDTO = userRepository.getUserDTOByTelegramId(user.getId().toString());
        if(userDTO == null) {
            userDTO = UserDTO.UserDTOBuilder.anUserDTO().withTelegramId(user.getId().toString())
                    .withUserName(user.getUserName())
                    .withAuthenticationDTO(authenticationDTO)
                    .withClientDTO(client)
                    .build();
        } else {
            userDTO.setClient(client);
            userDTO.setAuthenticationDTO(authenticationDTO);
            userDTO.setUserName(user.getUserName());
        }
        return userRepository.save(userDTO);
    }
}
