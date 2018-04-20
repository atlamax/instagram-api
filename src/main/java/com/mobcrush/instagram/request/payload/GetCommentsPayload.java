package com.mobcrush.instagram.request.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetCommentsPayload {

    @JsonProperty("last_comment_ts")
    private long lastCommentTimestamp;
    @JsonProperty("num_comments_requested")
    private int count;

    public long getLastCommentTimestamp() {
        return lastCommentTimestamp;
    }

    public void setLastCommentTimestamp(long lastCommentTimestamp) {
        this.lastCommentTimestamp = lastCommentTimestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("lastCommentTimestamp", lastCommentTimestamp)
                .append("count", count)
                .toString();
    }
}
