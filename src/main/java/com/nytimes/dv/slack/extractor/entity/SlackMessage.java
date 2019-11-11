package com.nytimes.dv.slack.extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nytimes.dv.slack.extractor.service.SlackTsDeserializer;
import com.nytimes.dv.slack.extractor.service.SlackTsSerializer;
import com.nytimes.dv.slack.extractor.service.StopWords;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(indexName = "slack_messages_v15")
public class SlackMessage {

    @Id
    @JsonProperty("client_msg_id")
    private String id;

    private String type;

    private String user;

    private String text;

    @JsonDeserialize(using = SlackTsDeserializer.class)
    @JsonSerialize(using = SlackTsSerializer.class)
    private LocalDateTime ts;

    private List<String> tags;

    public SlackMessage loadTags() {
        final String[] parts = text.split("\\s");
        this.tags = Arrays.stream(parts).parallel()
                .map(w -> w.toLowerCase()
                        .trim()
                        .replaceAll("[^a-z0-9]", ""))
                .filter(StopWords::isNot)
                .filter(w -> w.length() > 1)
                .collect(Collectors.toList());
        return this;
    }
}
