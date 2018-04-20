package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.request.payload.GetCommentsPayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;

public class GetCommentsRequest extends InstagramPostRequest<CommentsResponse> {

    private GetCommentsPayload payload;
    private String broadcastId;

    public GetCommentsRequest(GetCommentsPayload payload, String broadcastId) {
        this.payload = payload;
        this.broadcastId = broadcastId;
    }

    @Override
    public String getUrl() {
        return "live/" + broadcastId + "/get_comment/";
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
    public CommentsResponse parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, CommentsResponse.class);
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}
