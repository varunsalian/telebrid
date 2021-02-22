package com.telegrambot.realdebrid.services.api;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.telegrambot.realdebrid.exceptions.ConnectionException;
import com.telegrambot.realdebrid.services.dtos.Authentication;
import com.telegrambot.realdebrid.services.dtos.Client;
import com.telegrambot.realdebrid.services.dtos.UserDTO;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

public interface IRealDebridService {


    //AUTHENTICATION RELATED
    Authentication getRDToken()throws IOException, ConnectionException;
    Client waitForLogin(Authentication authenticationDTO) throws InterruptedException, ConnectionException, IOException;
    UserDTO getRDSession(UserDTO userDTO) throws MalformedURLException, JsonProcessingException;
    Map<String, Object> getInstantAvailabilityMapOfHashes(List<String> hashes, String accessToken)  throws IOException, ConnectionException;
}
