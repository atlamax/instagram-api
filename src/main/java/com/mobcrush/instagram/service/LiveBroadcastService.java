package com.mobcrush.instagram.service;

import com.mobcrush.instagram.domain.CreateLiveResponse;
import com.mobcrush.instagram.request.*;
import com.mobcrush.instagram.request.payload.AddLiveToStoriesPayload;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import com.mobcrush.instagram.request.payload.EndLivePayload;
import com.mobcrush.instagram.request.payload.StartLivePayload;
import org.apache.http.client.utils.URIBuilder;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.URI;
import java.net.URISyntaxException;

import static org.apache.http.util.Asserts.notNull;

public class LiveBroadcastService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiveBroadcastService.class);

    private Instagram4j instagram;

    private static final String PREVIEW_WIDTH = "720";
    private static final String PREVIEW_HEIGHT = "1184";
    private static final String RTMP_SCHEME = "rtmp";
    private static final int RTMP_PORT = 80;

    /**
     * Default constructor
     *
     * @param instagram instagram
     */
    public LiveBroadcastService(Instagram4j instagram) {
        this.instagram = instagram;
    }

    /**
     * Start live broadcasting
     *
     * @return URI for broadcasting
     */
    @Nullable
    public CreateLiveResponse start() {
        String csrfToken;
        try {
            csrfToken = instagram.getOrFetchCsrf();
        } catch (Exception e) {
            LOGGER.error("Error occurred during request for CSRF token", e);
            return null;
        }

        CreateLiveRequest createRequest = buildCreateRequest(instagram.getUuid(), csrfToken);
        CreateLiveResponse createResponse = sendRequest(createRequest);
        if (createResponse == null) {
            return null;
        }

        StartLiveRequest startRequest = buildStartRequest(
                instagram.getUuid(), csrfToken, createResponse.getBroadcastId()
        );
        sendRequest(startRequest);

        updateBroadcastingURL(createResponse);

        return createResponse;
    }

    /**
     * End live broadcasting
     *
     * @param broadcastId broadcast Id
     */
    public void end(@Nonnull String broadcastId) {
        notNull(instagram, "Instagram object must not be null");

        String csrfToken;
        try {
            csrfToken = instagram.getOrFetchCsrf();
        } catch (Exception e) {
            LOGGER.error("Error occurred during request for CSRF token", e);
            return;
        }

        EndLiveRequest endRequest = buildEndRequest(csrfToken, broadcastId);
        sendRequest(endRequest);

        AddLiveToStoriesRequest addToStoriesRequest = buildAddToStoriesRequest(csrfToken, broadcastId);
        sendRequest(addToStoriesRequest);
    }

    /**
     * Build request to create broadcast
     *
     * @param uuid      UUID
     * @param csrfToken CSRF token
     *
     * @return request
     */
    private CreateLiveRequest buildCreateRequest(String uuid, String csrfToken) {
        CreateLivePayload payload = new CreateLivePayload();
        payload.setUuid(uuid);
        payload.setCsrfToken(csrfToken);
        payload.setPreviewHeight(PREVIEW_HEIGHT);
        payload.setPreviewWidth(PREVIEW_WIDTH);
        payload.setBroadcastMessage("");
        payload.setBroadcastType(RTMP_SCHEME);
        payload.setInternalOnly("0");

        return new CreateLiveRequest(payload);
    }

    /**
     * Build request to start broadcasting
     *
     * @param uuid        UUID
     * @param csrfToken   CSRF token
     * @param broadcastId broadcast Id
     *
     * @return request
     */
    private StartLiveRequest buildStartRequest(String uuid, String csrfToken, String broadcastId) {
        StartLivePayload startLivePayload = new StartLivePayload();
        startLivePayload.setUuid(uuid);
        startLivePayload.setCsrfToken(csrfToken);
        startLivePayload.setShouldSendNotifications("1");

        return new StartLiveRequest(startLivePayload, broadcastId);
    }

    /**
     * Build request to end broadcasting
     *
     * @param csrfToken   CSRF token
     * @param broadcastId broadcast Id
     *
     * @return request
     */
    private EndLiveRequest buildEndRequest(String csrfToken, String broadcastId) {
        EndLivePayload payload = new EndLivePayload();
        payload.setUid(String.valueOf(instagram.getUserId()));
        payload.setUuid(instagram.getUuid());
        payload.setCsrfToken(csrfToken);

        return new EndLiveRequest(payload, broadcastId);
    }

    /**
     * Build request to add finished broadcast to "Stories"
     *
     * @param csrfToken   CSRF token
     * @param broadcastId broadcast Id
     *
     * @return request
     */
    private AddLiveToStoriesRequest buildAddToStoriesRequest(String csrfToken, String broadcastId) {
        AddLiveToStoriesPayload payload = new AddLiveToStoriesPayload();
        payload.setUid(String.valueOf(instagram.getUserId()));
        payload.setUuid(instagram.getUuid());
        payload.setCsrfToken(csrfToken);

        return new AddLiveToStoriesRequest(payload, broadcastId);
    }

    /**
     * Send request to Instagram
     *
     * @param request request
     * @param <T>     type of response
     *
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

    /**
     * Update URL for broadcasting
     *
     * @param response response model
     */
    private void updateBroadcastingURL(CreateLiveResponse response) {
        try {
            URI url = new URIBuilder(response.getUploadUrl())
                    .setScheme(RTMP_SCHEME)
                    .setPort(RTMP_PORT)
                    .build();
            response.setUploadUrl(url.toString());
        } catch (URISyntaxException e) {
            LOGGER.error("Error occurred while updating RTMP URL for live streaming");
        }
    }
}
