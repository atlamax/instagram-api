package com.mobcrush.instagram.request.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class EndLivePayload {

    @JsonProperty("_uid")
    private String uid;
    @JsonProperty("_uuid")
    private String uuid;
    @JsonProperty("_csrftoken")
    private String csrfToken;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

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

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("uid", uid)
                .append("uuid", uuid)
                .append("csrfToken", csrfToken)
                .toString();
    }
}
