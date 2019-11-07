package com.nytimes.dv.slack.extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SlackMetadata {

    @JsonProperty("next_cursor")
    private String nextCursor;
}
