package com.mobcrush.instagram.service.mediaserver;

import com.mobcrush.instagram.domain.mediaserver.LiveStream;

import javax.annotation.Nonnull;

public interface MediaServerService {

    @Nonnull
    LiveStream publish(@Nonnull String streamName);
}
