package com.nytimes.dv.slack.extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class SlackResult {

    private List<SlackMessage> messages;

    @JsonProperty("has_more")
    private Boolean more;

    @JsonProperty("response_metadata")
    private SlackMetadata metadata;

    @JsonIgnore
    public String getNextCursor() {
        return metadata.getNextCursor();
    }
}
