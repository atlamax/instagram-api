package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.request.payload.AddLiveToStoriesPayload;
import com.mobcrush.instagram.request.payload.EndLivePayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

public class AddLiveToStoriesRequest extends InstagramPostRequest<StatusResult> {

    private AddLiveToStoriesPayload payload;
    private String broadcastId;

    public AddLiveToStoriesRequest(AddLiveToStoriesPayload payload, String broadcastId) {
        this.payload = payload;
        this.broadcastId = broadcastId;
    }

    @Override
    public String getUrl() {
        return "live/" + broadcastId + "/add_to_post_live/";
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
