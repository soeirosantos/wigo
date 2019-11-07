package com.nytimes.dv.slack.extractor.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.nytimes.dv.slack.extractor.service.SlackTsDeserializer;
import com.nytimes.dv.slack.extractor.service.SlackTsSerializer;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Document(indexName = "slack_messages_v10")
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
}
