package com.mobcrush.instagram.domain.mediaserver.red5;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Red5LiveStreamStatisticsData {

    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("publish_name")
    private String publishName;
    @JsonProperty("state")
    private String state;
}
