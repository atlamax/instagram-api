package com.mobcrush.instagram.domain.mobcrush;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.mobcrush.instagram.json.StringEscapingSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ChatInlineMessage {

    @JsonProperty("text")
    @JsonSerialize(using = StringEscapingSerializer.class)
    private String text;
    @JsonProperty("senderName")
    @JsonSerialize(using = StringEscapingSerializer.class)
    private String senderName;
    @JsonProperty("profileLogoSmall")
    String profileImage;
}
