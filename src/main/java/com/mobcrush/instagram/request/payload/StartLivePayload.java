package com.mobcrush.instagram.request.payload;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class StartLivePayload {

    @JsonProperty("_uuid")
    private String uuid;
    @JsonProperty("_csrftoken")
    private String csrfToken;
    @JsonProperty("should_send_notifications")
    private String shouldSendNotifications;

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

    public String getShouldSendNotifications() {
        return shouldSendNotifications;
    }

    public void setShouldSendNotifications(String shouldSendNotifications) {
        this.shouldSendNotifications = shouldSendNotifications;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uuid", uuid)
                .append("csrfToken", csrfToken)
                .append("shouldSendNotifications", shouldSendNotifications)
                .toString();
    }
}
