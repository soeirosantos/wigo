package com.nytimes.dv.slack.extractor.service;

import com.nytimes.dv.slack.extractor.entity.SlackUserInfo;
import com.nytimes.dv.slack.extractor.entity.SlackUserInfoResult;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Component
public class SlackUserInfoRetriever {

    private static final String SLACK_API_USERS_INFO = "/users.info";

    private final String slackToken;
    private final WebClient webClient;

    public SlackUserInfoRetriever(@Value("${slack.token}") String slackToken, WebClient webClient) {
        this.slackToken = slackToken;
        this.webClient = webClient;
    }

    @SneakyThrows
    @Cacheable("userInfo")
    public SlackUserInfo get(String userId) {
        final WebClient.ResponseSpec res = webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(SLACK_API_USERS_INFO)
                        .queryParam("token", slackToken)
                        .queryParam("user", userId)
                        .build())
                .retrieve();

        final Mono<ResponseEntity<SlackUserInfoResult>> responseEntityMono = res.toEntity(SlackUserInfoResult.class);
        return Objects.requireNonNull(Objects.requireNonNull(responseEntityMono.block()).getBody()).getUser();
    }
}
