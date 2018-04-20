package com.mobcrush.instagram.service;

import com.mobcrush.instagram.request.HeartbeatRequest;
import com.mobcrush.instagram.request.payload.HeartbeatPayload;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;

import static org.apache.http.util.Asserts.notNull;

public class LiveHeartbeatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiveHeartbeatService.class);

    private Instagram4j instagram;

    /**
     * Default constructor
     *
     * @param instagram instagram
     */
    public LiveHeartbeatService(Instagram4j instagram) {
        this.instagram = instagram;
    }

    public void perform(@Nonnull String broadcastId) {
        notNull(broadcastId, "Broadcast Id must not be null");

        String csrfToken;
        try {
            csrfToken = instagram.getOrFetchCsrf();
        } catch (Exception e) {
            LOGGER.error("Error occurred during request for CSRF token", e);
            return;
        }

        HeartbeatRequest request = buildRequest(csrfToken, broadcastId);
        sendRequest(request);
    }

    /**
     * Build request to heartbeat
     *
     * @param csrfToken   CSRF token
     * @param broadcastId broadcast Id
     * @return request
     */
    private HeartbeatRequest buildRequest(String csrfToken, String broadcastId) {
        HeartbeatPayload payload = new HeartbeatPayload();
        payload.setCsrfToken(csrfToken);
        payload.setUuid(instagram.getUuid());

        return new HeartbeatRequest(payload, broadcastId);
    }

    /**
     * Send request to Instagram
     *
     * @param request request
     * @param <T>     type of response
     * @return response
     */
    private <T> T sendRequest(InstagramRequest<T> request) {
        try {
            return instagram.sendRequest(request);
        } catch (Exception e) {
            LOGGER.error("Error occurred during performing request", e);
            return null;
        }
    }
}
