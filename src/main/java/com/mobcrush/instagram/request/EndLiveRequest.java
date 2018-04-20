package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import com.mobcrush.instagram.request.payload.EndLivePayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

public class EndLiveRequest extends InstagramPostRequest<StatusResult> {

    private EndLivePayload payload;
    private String broadcastId;

    public EndLiveRequest(EndLivePayload payload, String broadcastId) {
        this.payload = payload;
        this.broadcastId = broadcastId;
    }

    @Override
    public String getUrl() {
        return "live/" + broadcastId + "/end_broadcast/";
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
    public StatusResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, StatusResult.class);
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}
