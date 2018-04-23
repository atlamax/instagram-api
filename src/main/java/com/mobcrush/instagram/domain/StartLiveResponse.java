package com.mobcrush.instagram.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class StartLiveResponse extends StatusResult {

    @JsonProperty("media_id")
    private String mediaId;

    public String getMediaId() {
        return mediaId;
    }

    public void setMediaId(String mediaId) {
        this.mediaId = mediaId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("mediaId", mediaId)
                .appendSuper(super.toString())
                .toString();
    }
}
