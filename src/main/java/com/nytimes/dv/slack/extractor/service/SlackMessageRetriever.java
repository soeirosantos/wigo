package com.nytimes.dv.slack.extractor.service;

import com.nytimes.dv.slack.extractor.entity.SlackMessageResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;

@Component
public class SlackMessageRetriever {

    private static final String SLACK_API_CONVERSATION_HISTORY = "/conversations.history";

    private final String slackChannel;
    private final String slackToken;
    private final WebClient webClient;

    public SlackMessageRetriever(@Value("${slack.channel}") String slackChannel,
                                 @Value("${slack.token}") String slackToken, WebClient webClient) {
        this.slackChannel = slackChannel;
        this.slackToken = slackToken;
        this.webClient = webClient;
    }

    public SlackMessageResult list(Integer fromDays) {
        return list(null, fromDays);
    }

    @SneakyThrows
    public SlackMessageResult list(String cursor, Integer fromDays) {

        final WebClient.ResponseSpec res = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SLACK_API_CONVERSATION_HISTORY)
                        .queryParam("token", slackToken)
                        .queryParam("channel", slackChannel)
                        .queryParam("limit", "100")
                        .queryParam("cursor", cursor)
                        .queryParam("inclusive", true)
                        .queryParam("oldest", LocalDateTime.now().minusDays(fromDays).toEpochSecond(ZoneOffset.UTC))
                        .build())
                .retrieve();

        final Mono<ResponseEntity<SlackMessageResult>> responseEntityMono = res.toEntity(SlackMessageResult.class);
        return Objects.requireNonNull(responseEntityMono.block()).getBody();
    }
}
