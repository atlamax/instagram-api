package com.mobcrush.instagram.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobcrush.instagram.request.payload.CreateLivePayload;
import org.brunocvcunha.instagram4j.requests.InstagramPostRequest;

public class CreateLiveRequest extends InstagramPostRequest<CreateLiveResult> {

    private CreateLivePayload payload;

    public CreateLiveRequest(CreateLivePayload payload) {
        this.payload = payload;
    }

    @Override
    public String getUrl() {
        return "live/create/";
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
    public CreateLiveResult parseResult(int statusCode, String content) {
        return parseJson(statusCode, content, CreateLiveResult.class);
    }

    @Override
    public boolean requiresLogin() {
        return true;
    }

}
