package com.mobcrush.instagram.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateLiveResult extends StatusResult {

    @JsonProperty("broadcast_id")
    private String broadcastId;
    @JsonProperty("upload_url")
    private String uploadUrl;

    public String getBroadcastId() {
        return broadcastId;
    }

    public void setBroadcastId(String broadcastId) {
        this.broadcastId = broadcastId;
    }

    public String getUploadUrl() {
        return uploadUrl;
    }

    public void setUploadUrl(String uploadUrl) {
        this.uploadUrl = uploadUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("broadcastId", broadcastId)
                .append("uploadUrl", uploadUrl)
                .appendSuper(super.toString())
                .toString();
    }
}
