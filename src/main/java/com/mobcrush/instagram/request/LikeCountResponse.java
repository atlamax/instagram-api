package com.mobcrush.instagram.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LikeCountResponse extends StatusResult {

    @JsonProperty("like_ts")
    private long timestamp;
    @JsonProperty("likes")
    private int count;
    @JsonProperty("burst_likes")
    private int burstLikes;
    @JsonProperty("likers")
    private List<User> users;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getBurstLikes() {
        return burstLikes;
    }

    public void setBurstLikes(int burstLikes) {
        this.burstLikes = burstLikes;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("timestamp", timestamp)
                .append("count", count)
                .append("burstLikes", burstLikes)
                .append("users", users)
                .appendSuper(super.toString())
                .toString();
    }
}
