package com.mobcrush.instagram.service;

import com.google.common.collect.Lists;
import com.mobcrush.instagram.domain.Comment;
import com.mobcrush.instagram.domain.CommentsResponse;
import com.mobcrush.instagram.domain.LikeCountResponse;
import com.mobcrush.instagram.request.GetCommentsRequest;
import com.mobcrush.instagram.request.LikeCountRequest;
import com.mobcrush.instagram.request.payload.GetCommentsPayload;
import com.mobcrush.instagram.request.payload.LikeCountPayload;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

public class BroadcastDataService {

    private static final Logger LOGGER = LoggerFactory.getLogger(BroadcastDataService.class);

    private static final int COMMENTS_COUNT = 10;

    private Instagram4j instagram;
    private long commentTimestamp = 0;
    private long likeTimestamp = 0;

    /**
     * Default constructor
     *
     * @param instagram instagram
     */
    public BroadcastDataService(Instagram4j instagram) {
        this.instagram = instagram;
    }

    @Nonnull
    public List<Comment> getComments(@Nonnull String broadcastId) {
        notNull(broadcastId, "Broadcast Id must not be null");
        List<Comment> result = Lists.newArrayList();

        GetCommentsRequest request = buildGetCommentsRequest(broadcastId);
        CommentsResponse response = sendRequest(request);
        if (response == null) {
            return result;
        }

        if (response.getCount() > 0) {
            for (Comment comment: response.getComments()) {
                if (comment.getCreatedDate() > commentTimestamp) {
                    result.add(comment);
                }
            }
        }
        updateTimestamp(response);

        return result;
    }

    @Nullable
    public LikeCountResponse getLikes(@Nonnull String broadcastId) {
        notNull(broadcastId, "Broadcast Id must not be null");

        LikeCountRequest request = buildLikeCountRequest(broadcastId);
        LikeCountResponse response = sendRequest(request);
        if (response == null) {
            return null;
        }

        likeTimestamp = response.getTimestamp();

        return response;
    }

    /**
     * Build request to get comments
     *
     * @param broadcastId broadcast Id
     *
     * @return request
     */
    private GetCommentsRequest buildGetCommentsRequest(String broadcastId) {
        GetCommentsPayload payload = new GetCommentsPayload();
        payload.setCount(COMMENTS_COUNT);
        payload.setLastCommentTimestamp(commentTimestamp);

        return new GetCommentsRequest(payload, broadcastId);
    }

    /**
     * Build request to get like count
     *
     * @param broadcastId broadcast Id
     *
     * @return request
     */
    private LikeCountRequest buildLikeCountRequest(String broadcastId) {
        LikeCountPayload payload = new LikeCountPayload();
        payload.setTimestamp(likeTimestamp);

        return new LikeCountRequest(payload, broadcastId);
    }

    /**
     * Send request to Instagram
     *
     * @param request request
     * @param <T>     type of response
     * @return response
     */
    private <T> T sendRequest(InstagramRequest<T> request) {
        try {
            return instagram.sendRequest(request);
        } catch (Exception e) {
            LOGGER.error("Error occurred during performing request", e);
            return null;
        }
    }

    private void updateTimestamp(CommentsResponse response) {
        if (response.getCount() == 0 && response.getSystemComments() == null) {
            return;
        }

        long lastCommentTimestamp = 0;
        if (response.getComments() != null && !response.getComments().isEmpty()) {
            Optional<Comment> lastComment = response.getComments()
                    .stream()
                    .max(Comparator.comparing(Comment::getCreatedDate));
            lastCommentTimestamp = lastComment.get().getCreatedDate();
        }

        long lastSystemCommentTimestamp = 0;
        if (response.getSystemComments() != null && !response.getSystemComments().isEmpty()) {
            Optional<Comment> lastSystemComment = response.getSystemComments()
                    .stream()
                    .max(Comparator.comparing(Comment::getCreatedDate));
            lastSystemCommentTimestamp = lastSystemComment.get().getCreatedDate();
        }

        commentTimestamp = Math.max(
                lastCommentTimestamp,
                lastSystemCommentTimestamp
        );
    }
}
