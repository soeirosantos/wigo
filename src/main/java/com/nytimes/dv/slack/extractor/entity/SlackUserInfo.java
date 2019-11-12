package com.nytimes.dv.slack.extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SlackUserInfo {

    private String id;

    private String name;

    @JsonProperty("real_name")
    private String realName;

    private SlackUserProfile profile;
}
