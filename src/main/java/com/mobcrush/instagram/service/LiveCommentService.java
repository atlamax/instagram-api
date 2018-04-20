package com.mobcrush.instagram.service;

import com.mobcrush.instagram.request.Comment;
import com.mobcrush.instagram.request.CommentsResponse;
import com.mobcrush.instagram.request.GetCommentsRequest;
import com.mobcrush.instagram.request.payload.GetCommentsPayload;
import org.brunocvcunha.instagram4j.Instagram4j;
import org.brunocvcunha.instagram4j.requests.InstagramRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Comparator;
import java.util.Optional;

import static org.apache.http.util.Asserts.notNull;

public class LiveCommentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LiveCommentService.class);

    private static final int COMMENTS_COUNT = 3;

    private Instagram4j instagram;
    private long lastCommentTimestamp = 0;

    /**
     * Default constructor
     *
     * @param instagram instagram
     */
    public LiveCommentService(Instagram4j instagram) {
        this.instagram = instagram;
    }

    @Nullable
    public CommentsResponse get(@Nonnull String broadcastId) {
        notNull(broadcastId, "Broadcast Id must not be null");

        String csrfToken;
        try {
            csrfToken = instagram.getOrFetchCsrf();
        } catch (Exception e) {
            LOGGER.error("Error occurred during request for CSRF token", e);
            return null;
        }

        GetCommentsRequest getRequest = buildGetRequest(csrfToken, broadcastId);
        CommentsResponse response = sendRequest(getRequest);
        if (response == null) {
            return null;
        }

        if (response.getCount() > 0) {
            updateTimestamp(response);
        }

        return response;
    }

    /**
     * Build request to add finished broadcast to "Stories"
     *
     * @param csrfToken   CSRF token
     * @param broadcastId broadcast Id
     * @return request
     */
    private GetCommentsRequest buildGetRequest(String csrfToken, String broadcastId) {
        GetCommentsPayload payload = new GetCommentsPayload();
        payload.setCount(COMMENTS_COUNT);
        payload.setLastCommentTimestamp(lastCommentTimestamp);

        return new GetCommentsRequest(payload, broadcastId);
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
        Optional<Comment> lastComment = response.getComments()
                .stream()
                .min(Comparator.comparing(Comment::getCreatedDate));

        Optional<Comment> lastSystemComment = response.getSystemComments()
                .stream()
                .min(Comparator.comparing(Comment::getCreatedDate));

        lastCommentTimestamp = Math.max(
                lastComment.get().getCreatedDate(),
                lastSystemComment.get().getCreatedDate()
        );
    }
}
