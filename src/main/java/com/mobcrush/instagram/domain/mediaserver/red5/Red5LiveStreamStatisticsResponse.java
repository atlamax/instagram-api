package com.mobcrush.instagram.domain.mediaserver.red5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Red5LiveStreamStatisticsResponse {

    @JsonProperty("status")
    private String status;
    @JsonProperty("code")
    private int code;
    @JsonProperty("data")
    private Red5LiveStreamStatisticsData data;

}
