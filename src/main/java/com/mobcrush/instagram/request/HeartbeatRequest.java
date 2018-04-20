package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.request.payload.AddLiveToStoriesPayload;
import com.mobcrush.instagram.request.payload.HeartbeatPayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

public class HeartbeatRequest extends InstagramPostRequest<HeartbeatResponse> {

    private HeartbeatPayload payload;
    private String broadcastId;

    public HeartbeatRequest(HeartbeatPayload payload, String broadcastId) {
        this.payload = payload;
        this.broadcastId = broadcastId;
    }

    @Override
    public String getUrl() {
        return "live/" + broadcastId + "/heartbeat_and_get_viewer_count/";
    }

    @Override
    public String getPayload() {
        ObjectMapper mapper = new ObjectMapper();
        String payloadJson = null;
        try {
            payloadJson = mapper.writeValueAsString(payload);
        } catch (Exception e) {

        }

        return payloadJson;
    }

    @Override
    public HeartbeatResponse parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, HeartbeatResponse.class);
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}
