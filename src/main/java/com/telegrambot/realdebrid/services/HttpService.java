package com.telegrambot.realdebrid.services;


import com.telegrambot.realdebrid.common.CommonConstants;
import com.telegrambot.realdebrid.exceptions.ConnectionException;
import com.telegrambot.realdebrid.services.api.IHttpService;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;


@Service
public class HttpService implements IHttpService {

    @Override
    public String httpGet(URL url) throws ConnectionException {
        try {
            HttpURLConnection httpURLConnection = getHttpConnection(url, null);
            if (!(httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode()<=399)) {
                throw new ConnectionException(CommonConstants.UNABLE_TO_CONNECT + httpURLConnection.getResponseCode());
            }
            String jsonString = readJSONFromInputStream(httpURLConnection.getInputStream());
            if (jsonString == null)
                throw new ConnectionException("Unable to connect");
            return jsonString;
        } catch (ConnectionException | IOException e) {
            e.printStackTrace();
            throw new ConnectionException("Unable to connect");
        }
    }

    @Override
    public String httpGet(String url) throws MalformedURLException, ConnectionException {
        return httpGet(new URL(url));
    }

    @Override
    public String httpGet(URL url, String bearerToken) throws ConnectionException {
        HttpURLConnection httpURLConnection = null;
        if(bearerToken == null)
                throw new ConnectionException("Invalid Token");
        try {
            httpURLConnection = getHttpConnection(url, bearerToken);
            if (!(httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode()<=399)) {
                throw new ConnectionException(CommonConstants.UNABLE_TO_CONNECT + httpURLConnection.getResponseCode());
            }
            String jsonString = readJSONFromInputStream(httpURLConnection.getInputStream());
            if (jsonString == null)
                throw new ConnectionException("Unable to connect");
            return jsonString;
        } catch (IOException | ConnectionException e) {
            e.printStackTrace();
            throw new ConnectionException("Unable to connect");
        }
    }

    @Override
    public String httpGet(String url, String bearerToken) throws MalformedURLException, ConnectionException {
        return httpGet(new URL(url), bearerToken);
    }

    @Override
    public String httpPost(URL url, String accessToken, Map<String, String> payloadData) {
        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            setUrlPropertiesForPost(httpURLConnection, accessToken);
            String data = generateUrlEncodedAttributesForPost(payloadData);
            byte[] postData = data.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            httpURLConnection.setRequestProperty(CommonConstants.CONTENT_LENGTH, Integer.toString(postDataLength));
            httpURLConnection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
            dataOutputStream.write(postData);
            dataOutputStream.flush();

            if (httpURLConnection.getResponseCode() >= 200 && httpURLConnection.getResponseCode()<=399) {
                String jsonString = readJSONFromInputStream(httpURLConnection.getInputStream());
                if (jsonString != null)
                    return jsonString;
                else
                    throw new ConnectionException("Unable to connect");
            } else
                throw new ConnectionException("Unable to connect");

        } catch (IOException | ConnectionException e) {
            e.printStackTrace();
        } finally {
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }

    @Override
    public String httpPost(URL url, Map<String, String> payloadData) {
        return httpPost(url,null, payloadData);
    }

    @Override
    public String httpPost(String url, Map<String, String> payloadData) throws MalformedURLException {
        return httpPost(new URL(url), payloadData);
    }

    @Override
    public String httpPost(String url,String accessToken, Map<String, String> payloadData) throws MalformedURLException {
        return httpPost(new URL(url), accessToken, payloadData);
    }

    @Override
    public HttpURLConnection getHttpConnection(URL url, String bearerToken) throws IOException {
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(CommonConstants.HTTP_GET);
        httpURLConnection.setRequestProperty(CommonConstants.USER_AGENT, CommonConstants.USER_AGENT_DETAILS);
        if(bearerToken != null) {
            String bearer = CommonConstants.BEARER + bearerToken;
            httpURLConnection.setRequestProperty(CommonConstants.AUTHORIZATION, bearer);
        }
        httpURLConnection.connect();
        return httpURLConnection;
    }

    @Override
    public HttpURLConnection getHttpUrlConnectionWithToken(URL url, String token) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod(CommonConstants.HTTP_GET);
        conn.setRequestProperty(CommonConstants.USER_AGENT, CommonConstants.USER_AGENT_DETAILS);
        if (token != null) {
            String bearer = CommonConstants.BEARER + token;
            conn.setRequestProperty(CommonConstants.AUTHORIZATION, bearer);
        }
        conn.connect();
        return conn;
    }

    private String readJSONFromInputStream(InputStream inputStream) throws IOException {
        return IOUtils.toString(inputStream, StandardCharsets.UTF_8);
    }

    private String generateUrlEncodedAttributesForPost(Map<String, String> hashMap) {
        StringBuilder data = new StringBuilder(CommonConstants.EMPTY_STRING);
        int i=0;
        for (Map.Entry<String, String> entry: hashMap.entrySet()){
            if(i++==0){
                data.append(entry.getKey()).append(CommonConstants.SYMBOL_EQUALS).append(entry.getValue());
            } else {
                data.append(CommonConstants.SYMBOL_AND).append(entry.getKey()).append(CommonConstants.SYMBOL_EQUALS).append(entry.getValue());
            }
        }
        return data.toString();
    }

    private void setUrlPropertiesForPost(HttpURLConnection urlConnection, String accessToken) throws ProtocolException {
        urlConnection.setRequestMethod(CommonConstants.HTTP_POST);
        urlConnection.setDoOutput(true);
        urlConnection.setInstanceFollowRedirects(false);
        urlConnection.setRequestProperty(CommonConstants.CONTENT_TYPE, CommonConstants.URL_ENCODED_CONTENT_TYPE);
        urlConnection.setRequestProperty(CommonConstants.CHARSET, CommonConstants.UTF_8);
        if (accessToken != null) {
            String bearer = CommonConstants.BEARER + accessToken;
            urlConnection.setRequestProperty(CommonConstants.AUTHORIZATION, bearer);
        }
        urlConnection.setRequestProperty(CommonConstants.USER_AGENT, CommonConstants.USER_AGENT_DETAILS);
    }
}
