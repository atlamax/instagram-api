package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.request.payload.AddLiveToStoriesPayload;
import com.mobcrush.instagram.request.payload.LikeCountPayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

public class LikeCountRequest extends InstagramPostRequest<LikeCountResponse> {

    private LikeCountPayload payload;
    private String broadcastId;

    public LikeCountRequest(LikeCountPayload payload, String broadcastId) {
        this.payload = payload;
        this.broadcastId = broadcastId;
    }

    @Override
    public String getUrl() {
        return "live/" + broadcastId + "/get_like_count/";
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
    public LikeCountResponse parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, LikeCountResponse.class);
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}
