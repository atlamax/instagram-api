package com.mobcrush.instagram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.domain.CreateLiveResponse;
import com.mobcrush.instagram.domain.StartLiveResponse;
import org.apache.commons.io.IOUtils;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.apache.http.util.Asserts.notNull;

public class InstagramBroadcastingService {

    private static final Logger LOGGER = LoggerFactory.getLogger(InstagramBroadcastingService.class);

    private static final int PREVIEW_WIDTH = 720;
    private static final int PREVIEW_HEIGHT = 1184;
    private static final String API_BASE_URL = "https://i.instagram.com/api/v1/";

    private ObjectMapper MAPPER = new ObjectMapper();

    private static CloseableHttpClient httpClient = buildHttpClient();

    public void create(@Nonnull String uuid, @Nonnull String token) {
        notNull(uuid, "UUID must not be null");
        notNull(token, "Token must not be null");

        LOGGER.info("Going to create broadcast with UUID '{}' and token '{}'", uuid, token);
        HttpPost request = new HttpPost(API_BASE_URL + "live/create/");
        CloseableHttpResponse response;

        try {
            request.setEntity(new UrlEncodedFormEntity(getCreateRequestBodyParams(uuid, token)));
            response = httpClient.execute(request);
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
            return;
        }

        LOGGER.info("Response from Instagram with status {}", response.getStatusLine().getStatusCode());

        CreateLiveResponse responseModel;
        try {
            String requestBody = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            LOGGER.info("Response body from Instagram: {}", requestBody);
            responseModel = MAPPER.readValue(requestBody, CreateLiveResponse.class);
        } catch (IOException e) {
            LOGGER.error("Error occur during reading input stream", e);
        }
    }

    public void start(@Nonnull String uuid, @Nonnull String token, @Nonnull String broadcastId) {
        notNull(uuid, "UUID must not be null");
        notNull(token, "Token must not be null");
        notNull(broadcastId, "Broadcast Id must not be null");

        LOGGER.info("Going to start streaming with UUID '{}', token '{}' and broadcastId '{}'", uuid, token, broadcastId);
        HttpPost request = new HttpPost(API_BASE_URL + "live/" + broadcastId + "/start/");
        CloseableHttpResponse response;

        try {
            request.setEntity(new UrlEncodedFormEntity(getLiveRequestBodyParams(uuid, token)));
            response = httpClient.execute(request);
        } catch (Exception e) {
            LOGGER.error("Error occurred", e);
            return;
        }

        LOGGER.info("Response from Instagram with status {}", response.getStatusLine().getStatusCode());

        StartLiveResponse responseModel;
        try {
            String requestBody = IOUtils.toString(response.getEntity().getContent(), Charset.forName("UTF-8"));
            LOGGER.info("Response body from Instagram: {}", requestBody);
            responseModel = MAPPER.readValue(requestBody, StartLiveResponse.class);
        } catch (IOException e) {
            LOGGER.error("Error occur during reading input stream", e);
        }

    }

    private List<NameValuePair> getCreateRequestBodyParams(String uuid, String token) {
        List<NameValuePair> result = new ArrayList<>();
        result.add(new BasicNameValuePair("_uuid", uuid));
        result.add(new BasicNameValuePair("_csrftoken", token));
        result.add(new BasicNameValuePair("preview_height", String.valueOf(PREVIEW_HEIGHT)));
        result.add(new BasicNameValuePair("preview_width", String.valueOf(PREVIEW_WIDTH)));
        result.add(new BasicNameValuePair("broadcast_message", "?????"));
        result.add(new BasicNameValuePair("broadcast_type", "RTMP"));
        result.add(new BasicNameValuePair("internal_only", "0"));

        return result;
    }

    private List<NameValuePair> getLiveRequestBodyParams(String uuid, String token) {
        List<NameValuePair> result = new ArrayList<>();
        result.add(new BasicNameValuePair("_uuid", uuid));
        result.add(new BasicNameValuePair("_csrftoken", token));
        result.add(new BasicNameValuePair("should_send_notifications", "1"));

        return result;
    }

    private static CloseableHttpClient buildHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(30 * 1000)
                .setConnectionRequestTimeout(240 * 1000)
                .setRedirectsEnabled(true)
                .setMaxRedirects(8)
                .build();

        return HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
