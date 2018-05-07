package com.mobcrush.instagram.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.domain.mobcrush.ChatMessage;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.http.util.Asserts.notNull;

public class MobcrushService {

    private static final Logger LOGGER = LoggerFactory.getLogger(MobcrushService.class);

    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int SOCKET_TIMEOUT = 1000;

    private ObjectMapper objectMapper;
    private CloseableHttpAsyncClient httpClient;
    private URI uri;

    private String chatRoomId;
    private String host;
    private String accessToken;

    public MobcrushService(@Nonnull String chatRoomId, @Nonnull String host, @Nonnull String accessToken) {
        notNull(chatRoomId, "Chat room ID must not be null");
        notNull(host, "Host must not be null");
        notNull(host, "Access token must not be null");

        this.chatRoomId = chatRoomId;
        this.host = host;
        this.accessToken = accessToken;

        objectMapper = new ObjectMapper();
        httpClient = getHttpClient();
        uri = buildURI();

        makePresenceCall(chatRoomId, host, accessToken);
    }

    /**
     * Publish message to Mobcrush platform
     *
     * @param message   message to publish
     */
    public void publish(ChatMessage message) {
        HttpPost httpPost = new HttpPost(uri);
        httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);
        httpPost.setHeader(HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType());

        try {
            String content = objectMapper.writeValueAsString(message);
            httpPost.setEntity(new StringEntity(content));
        } catch (Exception e) {
            LOGGER.error("Cannot publish message to Mobcrush", e);
            return;
        }

        httpClient.execute(httpPost, new MobcrushFutureCallback());
    }

    private void makePresenceCall(@Nonnull String chatRoomId, @Nonnull String host, @Nonnull String accessToken) {
        URI putURI;
        try {
            putURI = new URIBuilder(host)
                    .setPath("api/chatroom/" + chatRoomId + "/presence")
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error("Cannot make precense call to Mobcrush due to incorrect URL", e);
            return;
        }

        HttpPut httPut = new HttpPut(putURI);
        httPut.setHeader(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken);

        httpClient.execute(httPut, new MobcrushPresenceFutureCallback());
    }

    private CloseableHttpAsyncClient getHttpClient() {
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .setRedirectsEnabled(true)
                .setRelativeRedirectsAllowed(true)
                .setCircularRedirectsAllowed(true)
                .build();

        CloseableHttpAsyncClient result = HttpAsyncClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
        result.start();

        return result;
    }

    private URI buildURI() {
        try {
            return new URIBuilder(host)
                    .setPath("api/chatroom/" + chatRoomId + "/chatmessage")
                    .build();
        } catch (URISyntaxException e) {
            LOGGER.error("Cannot build URI for Mobcrush", e);
            throw new IllegalArgumentException(e);
        }
    }

    /**
     * Custom callback on request
     */
    private class MobcrushFutureCallback implements FutureCallback<HttpResponse> {

        public void failed(Exception e) {
            LOGGER.error("Failed with exception", e);
        }

        public void completed(HttpResponse response) {
            int code = response.getStatusLine().getStatusCode();

            if(HttpStatus.SC_OK == code){
                LOGGER.info("Message published to Mobcrush");
            } else if (HttpStatus.SC_UNAUTHORIZED == code) {
                LOGGER.error("Request to Mobcrush is not authorized");
            } else {
                LOGGER.error(
                        "Failed to send data to Mobcrush. Status: {}, reason: {}",
                        code, response.getStatusLine().getReasonPhrase()
                );
            }
        }

        public void cancelled() {
            LOGGER.warn("Cancelled request to Mobcrush");
        }
    }

    /**
     * Callback handler for Mobcrush "Presence" request
     */
    private class MobcrushPresenceFutureCallback implements FutureCallback<HttpResponse> {

        public void failed(Exception e) {
            LOGGER.error("Failed presence request to Mobcrush with exception", e);
        }

        public void completed(HttpResponse response) {
            LOGGER.info(
                    "Mobcrush presence response: status: {}, reason: {}",
                    response.getStatusLine().getStatusCode(), response.getStatusLine().getReasonPhrase()
            );
        }

        public void cancelled() {
            LOGGER.warn("Cancelled presence request to Mobcrush");
        }
    }

}
