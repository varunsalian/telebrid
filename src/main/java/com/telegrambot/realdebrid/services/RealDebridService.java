package com.telegrambot.realdebrid.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.telegrambot.realdebrid.common.CommonConstants;
import com.telegrambot.realdebrid.common.CommonService;
import com.telegrambot.realdebrid.exceptions.ConnectionException;
import com.telegrambot.realdebrid.services.api.IHttpService;
import com.telegrambot.realdebrid.services.api.IRealDebridService;
import com.telegrambot.realdebrid.services.dtos.Authentication;
import com.telegrambot.realdebrid.services.dtos.Client;
import com.telegrambot.realdebrid.services.dtos.Token;
import com.telegrambot.realdebrid.services.dtos.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class RealDebridService implements IRealDebridService {

    private static ObjectMapper objectMapper;

    @Autowired
    public void setHttpService(IHttpService httpService) {
        this.httpService = httpService;
    }
    private IHttpService httpService;

    @Autowired
    public void setCommonService(CommonService commonService) {
        this.commonService = commonService;
    }
    private CommonService commonService;

    @Override
    public Authentication getRDToken() throws IOException, ConnectionException {
        String authenticationJsonString = httpService.httpGet(CommonConstants.DEBRID_OAUTH_URL + CommonConstants.DEBRID_AUTHENTICATION_PATH);
        return objectMapper.readValue(authenticationJsonString, Authentication.class);
    }

    @Override
    public Client waitForLogin(Authentication authenticationDTO) throws InterruptedException, ConnectionException, IOException {
        String clientJsonString = repeatedCallToGetSecretId(authenticationDTO);
        if (clientJsonString == null) {
            return null;
        }
        return objectMapper.readValue(clientJsonString, Client.class);
    }

    @Override
    public UserDTO createRDSession(UserDTO userDTO) throws MalformedURLException, JsonProcessingException {
        Map<String, String> hashMap = generatePostData(userDTO.getAuthenticationDTO(), userDTO.getClient());
        String tokenJsonString = httpService.httpPost(CommonConstants.DEBRID_OAUTH_URL + CommonConstants.DEBRID_TOKEN_PATH, hashMap);
        if (tokenJsonString == null)
            return null;
        Token token = objectMapper.readValue(tokenJsonString, Token.class);
        userDTO.setToken(token);
        return userDTO;
    }

    private Map<String, String> generatePostData(Authentication authenticationDTO, Client client) {
        LinkedHashMap<String, String> postData = new LinkedHashMap<>();
        postData.put(CommonConstants.CLIENT_ID, client.getClientID());
        postData.put(CommonConstants.CLIENT_SECRET, client.getClientSecret());
        postData.put(CommonConstants.DEVICE_CODE, authenticationDTO.getDeviceCode());
        postData.put(CommonConstants.GRANT_TYPE, CommonConstants.GRANT_TYPE_URL);
        return postData;
    }

    private String repeatedCallToGetSecretId(Authentication authenticationDTO) throws IOException, InterruptedException, ConnectionException {
        URL url1 = new URL(CommonConstants.DEBRID_OAUTH_URL + CommonConstants.DEBRID_SECRET_ID_PATH + authenticationDTO.getDeviceCode());
        HttpURLConnection httpURLConnection;
        for (int i = 0; i < 120; i++) {
            httpURLConnection = httpService.getHttpUrlConnectionWithToken(url1, null);
            if (httpURLConnection.getResponseCode() == 200) {
                return commonService.readJSONFromInputStream(httpURLConnection.getInputStream());
            } else if (httpURLConnection.getResponseCode() == 400) {
                throw new ConnectionException(CommonConstants.URL_INVALID);
            }

            //TODO: look for a better way
            Thread.sleep(5000);
        }
        return null;
    }

    public RealDebridService() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
