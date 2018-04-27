package com.mobcrush.instagram.service.mediaserver.red5;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.domain.mediaserver.LiveStream;
import com.mobcrush.instagram.domain.mediaserver.red5.Red5LiveStreamStatisticsResponse;
import com.mobcrush.instagram.service.mediaserver.MediaServerService;
import com.sun.istack.internal.NotNull;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.http.util.Asserts.notNull;

public class Red5Service implements MediaServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Red5Service.class);

    private static final int RED5_API_PORT = 5080;
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int SOCKET_TIMEOUT = 1000;

    private String red5Host;
    private ObjectMapper objectMapper;
    private URI uri;
    private CloseableHttpClient httpClient;

    public Red5Service(@Nonnull String red5Host) {
        notNull(red5Host, "Host must not be null");

        this.red5Host = red5Host;

        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(SOCKET_TIMEOUT)
                .setConnectTimeout(CONNECTION_TIMEOUT)
                .build();

        httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    @Nonnull
    @Override
    public LiveStream publish(@Nonnull String streamName) {
        notNull(streamName, "Stream name must not be null");

        LiveStream result = LiveStream.builder()
                .name(streamName)
                .build();

        CloseableHttpResponse response;
        try {
            HttpGet httpGet = new HttpGet(buildURI(streamName));
            response = httpClient.execute(httpGet);
        } catch (IOException e) {
            LOGGER.error("Error occurred during request to Red5", e);
            return result;
        }

        InputStream inputStream;
        try {
            inputStream = response.getEntity().getContent();
        } catch (Exception e) {
            LOGGER.error("Error occurred during reading request from Red5", e);
            return result;
        }

        Red5LiveStreamStatisticsResponse statistics;
        try {
            statistics = objectMapper.readValue(inputStream, Red5LiveStreamStatisticsResponse.class);
        } catch (IOException e) {
            LOGGER.error("Error occurred during parsing request from Red5", e);
            return result;
        }

        convert(result, statistics);

        return result;
    }

    private void convert(LiveStream liveStream, Red5LiveStreamStatisticsResponse response) {
        liveStream.setContinue(
                response.getData().getState() != null
        );
    }

    private String buildURI(String streamName) {
        try {
            return new URIBuilder()
                    .setHost(red5Host)
                    .setPort(RED5_API_PORT)
                    .setPath("api/v1/applications/live/streams/" + streamName)
                    .setParameter("accessToken", "")
                    .build()
                    .toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot build URL to Red5 media server", e);
        }
    }

}
