package com.telegrambot.realdebrid.services.api;

import com.telegrambot.realdebrid.exceptions.ConnectionException;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public interface IHttpService {
    String httpGet(URL url) throws ConnectionException;
    String httpGet(String url) throws MalformedURLException, ConnectionException;
    String httpGet(URL url, String bearerToken) throws ConnectionException;
    String httpGet(String url, String bearerToken) throws MalformedURLException, ConnectionException;
    HttpURLConnection getHttpConnection(URL url, String bearerToken) throws IOException;

    String httpPost(String url, String accessToken, Map<String, String> payloadData) throws MalformedURLException;
    String httpPost(URL url, Map<String, String> payloadData);
    String httpPost(URL url, String accessToken, Map<String, String> payloadData);
    String httpPost(String url, Map<String, String> payloadData) throws MalformedURLException;
    HttpURLConnection getHttpUrlConnectionWithToken(URL url, String token) throws IOException;
}
