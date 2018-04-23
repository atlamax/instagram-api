package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.domain.StartLiveResponse;
import com.mobcrush.instagram.request.payload.StartLivePayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;

public class StartLiveRequest extends InstagramPostRequest<StartLiveResponse> {

    private StartLivePayload payload;
    private String broadcastId;

    public StartLiveRequest(StartLivePayload payload, String broadcastId) {
        this.payload = payload;
        this.broadcastId = broadcastId;
    }

    @Override
    public String getUrl() {
        return "live/" + broadcastId + "/start/";
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
    public StartLiveResponse parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, StartLiveResponse.class);
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}
