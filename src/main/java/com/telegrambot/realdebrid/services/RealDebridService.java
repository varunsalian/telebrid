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
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;
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
    public UserDTO getRDSession(UserDTO userDTO) throws MalformedURLException, JsonProcessingException {
        Map<String, String> hashMap = generatePostData(userDTO);
        String tokenJsonString = httpService.httpPost(CommonConstants.DEBRID_OAUTH_URL + CommonConstants.DEBRID_TOKEN_PATH, hashMap);
        if (tokenJsonString == null)
            return null;
        Token token = objectMapper.readValue(tokenJsonString, Token.class);
        userDTO.setToken(token);
        return userDTO;
    }

    @Override
    public Map<String, Object> getInstantAvailabilityMapOfHashes(List<String> hashes, String accessToken) throws IOException, ConnectionException {
        if (accessToken == null) {
            throw new ConnectionException("Credentials Unavailable");
        }
        String hashesString = convertHashesToRequestUrl(hashes);
        Map<String, Object> instantAvailabilityMap = null;
        String data = null;
        data = httpService.httpGet(new URL(CommonConstants.DEBRID_API_URL + CommonConstants.DEBRID_TORRENT_INSTANT_AVAILABILITY_PATH + hashesString), accessToken);
        JSONObject jsonObject =  null;
        if(data!=null) {
            jsonObject = new JSONObject(data);
        }
        if(jsonObject!=null) {
            instantAvailabilityMap = jsonObject.toMap();
        }
        return instantAvailabilityMap;
    }

    private String convertHashesToRequestUrl(List<String> hashes) {
        if(hashes==null || hashes.isEmpty())
            return null;

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < hashes.size(); i++) {
            if (i != hashes.size() - 1) {
                stringBuilder.append(hashes.get(i)).append(CommonConstants.FORWARD_SLASH);
            } else {
                stringBuilder.append(hashes.get(i));
            }
        }
        return stringBuilder.toString();
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

    private Map<String, String> generatePostData(UserDTO userDTO) {
        LinkedHashMap<String, String> postData = new LinkedHashMap<>();
        if (userDTO != null) {
            postData.put(CommonConstants.CLIENT_ID, userDTO.getClient().getClientID());
            postData.put(CommonConstants.CLIENT_SECRET, userDTO.getClient().getClientSecret());
            if (userDTO.getToken() != null && userDTO.getToken().getRefreshToken() != null)
                postData.put(CommonConstants.DEVICE_CODE, userDTO.getToken().getRefreshToken());
            else
                postData.put(CommonConstants.DEVICE_CODE, userDTO.getAuthenticationDTO().getDeviceCode());
            postData.put(CommonConstants.GRANT_TYPE, CommonConstants.GRANT_TYPE_URL);
        }
        return postData;
    }

    public RealDebridService() {
        objectMapper = new ObjectMapper();
        objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
        objectMapper.configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }
}
