package com.mobcrush.instagram.request.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class CreateLivePayload {

    @JsonProperty("_uuid")
    private String uuid;
    @JsonProperty("_csrftoken")
    private String csrfToken;
    @JsonProperty("preview_height")
    private String previewHeight;
    @JsonProperty("preview_width")
    private String previewWidth;
    @JsonProperty("broadcast_message")
    private String broadcastMessage;
    @JsonProperty("broadcast_type")
    private String broadcastType;
    @JsonProperty("internal_only")
    private String internalOnly;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getCsrfToken() {
        return csrfToken;
    }

    public void setCsrfToken(String csrfToken) {
        this.csrfToken = csrfToken;
    }

    public String getPreviewHeight() {
        return previewHeight;
    }

    public void setPreviewHeight(String previewHeight) {
        this.previewHeight = previewHeight;
    }

    public String getPreviewWidth() {
        return previewWidth;
    }

    public void setPreviewWidth(String previewWidth) {
        this.previewWidth = previewWidth;
    }

    public String getBroadcastMessage() {
        return broadcastMessage;
    }

    public void setBroadcastMessage(String broadcastMessage) {
        this.broadcastMessage = broadcastMessage;
    }

    public String getBroadcastType() {
        return broadcastType;
    }

    public void setBroadcastType(String broadcastType) {
        this.broadcastType = broadcastType;
    }

    public String getInternalOnly() {
        return internalOnly;
    }

    public void setInternalOnly(String internalOnly) {
        this.internalOnly = internalOnly;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uuid", uuid)
                .append("csrfToken", csrfToken)
                .append("previewHeight", previewHeight)
                .append("previewWidth", previewWidth)
                .append("broadcastMessage", broadcastMessage)
                .append("broadcastType", broadcastType)
                .append("internalOnly", internalOnly)
                .toString();
    }
}
