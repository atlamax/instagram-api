package com.mobcrush.instagram.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

@JsonIgnoreProperties(ignoreUnknown = true)
public class HeartbeatResponse extends StatusResult {

    @JsonProperty("broadcast_status")
    private String broadcastStatus;
    @JsonProperty("viewer_count")
    private int viewerCount;
    @JsonProperty("total_unique_viewer_count")
    private int uniqueViewerCount;

    public String getBroadcastStatus() {
        return broadcastStatus;
    }

    public void setBroadcastStatus(String broadcastStatus) {
        this.broadcastStatus = broadcastStatus;
    }

    public int getViewerCount() {
        return viewerCount;
    }

    public void setViewerCount(int viewerCount) {
        this.viewerCount = viewerCount;
    }

    public int getUniqueViewerCount() {
        return uniqueViewerCount;
    }

    public void setUniqueViewerCount(int uniqueViewerCount) {
        this.uniqueViewerCount = uniqueViewerCount;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("broadcastStatus", broadcastStatus)
                .append("viewerCount", viewerCount)
                .append("uniqueViewerCount", uniqueViewerCount)
                .appendSuper(super.toString())
                .toString();
    }
}
