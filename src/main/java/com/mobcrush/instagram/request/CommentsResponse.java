package com.mobcrush.instagram.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.brunocvcunha.instagram4j.requests.payload.StatusResult;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentsResponse extends StatusResult {

    @JsonProperty("comment_count")
    private int count;
    @JsonProperty("comments")
    private List<Comment> comments;
    @JsonProperty("system_comments")
    private List<Comment> systemComments;
    @JsonProperty("has_more_comments")
    private boolean hasMoreComments;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getSystemComments() {
        return systemComments;
    }

    public void setSystemComments(List<Comment> systemComments) {
        this.systemComments = systemComments;
    }

    public boolean isHasMoreComments() {
        return hasMoreComments;
    }

    public void setHasMoreComments(boolean hasMoreComments) {
        this.hasMoreComments = hasMoreComments;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("count", count)
                .append("comments", comments)
                .append("systemComments", systemComments)
                .append("hasMoreComments", hasMoreComments)
                .appendSuper(super.toString())
                .toString();
    }
}
