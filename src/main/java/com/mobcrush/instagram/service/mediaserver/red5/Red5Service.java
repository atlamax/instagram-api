package com.mobcrush.instagram.service.mediaserver.red5;

import com.mobcrush.instagram.domain.mediaserver.LiveStream;
import com.mobcrush.instagram.service.mediaserver.MediaServerService;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import java.io.IOException;
import java.net.URISyntaxException;

import static org.apache.http.util.Asserts.notNull;

public class Red5Service implements MediaServerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(Red5Service.class);

    private static final int RED5_API_PORT = 5080;
    private static final int CONNECTION_TIMEOUT = 1000;
    private static final int SOCKET_TIMEOUT = 1000;

    private String host;
    private String accessToken;
    private CloseableHttpClient httpClient;

    public Red5Service(@Nonnull String host, @Nonnull String accessToken) {
        notNull(host, "Host must not be null");
        notNull(accessToken, "Access token must not be null");

        this.host = host;
        this.accessToken = accessToken;

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

        CloseableHttpResponse response = null;
        try {
            HttpGet httpGet = new HttpGet(buildURI(streamName));
            response = httpClient.execute(httpGet);

            LOGGER.info("Response status from red5: " + response.getStatusLine().getStatusCode());
            result.setContinue(
                    HttpStatus.SC_OK == response.getStatusLine().getStatusCode()
            );

        } catch (IOException e) {
            LOGGER.error("Error occurred during request to Red5", e);
        } finally {
            if (response != null) {
                EntityUtils.consumeQuietly(response.getEntity());
                try {
                    response.close();
                } catch (IOException e) {
                    LOGGER.warn("Error occurred during closing HttpResponse", e);
                }
            }
        }

        return result;
    }

    private String buildURI(String streamName) {
        try {
            return new URIBuilder()
                    .setScheme("http")
                    .setHost(host)
                    .setPort(RED5_API_PORT)
                    .setPath("api/v1/applications/live/streams/" + streamName)
                    .setParameter("accessToken", accessToken)
                    .build()
                    .toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException("Cannot build URL to Red5 media server", e);
        }
    }

}
