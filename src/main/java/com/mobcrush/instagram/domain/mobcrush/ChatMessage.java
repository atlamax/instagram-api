package com.mobcrush.instagram.domain.mobcrush;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChatMessage {

    @JsonProperty("text")
    private String message;
    @JsonProperty("contentType")
    private String contentType = "application/x-mobcrush/message/instagram";

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("message", message)
                .append("contentType", contentType)
                .toString();
    }
}
