package com.mobcrush.instagram.domain.mediaserver;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LiveStream {

    private String name;
    private boolean isContinue;
}
