package com.nytimes.dv.slack.extractor;

import com.nytimes.dv.slack.extractor.entity.SlackMessage;
import com.nytimes.dv.slack.extractor.entity.SlackMessageResult;
import com.nytimes.dv.slack.extractor.repository.Elasticsearch;
import com.nytimes.dv.slack.extractor.service.SlackMessageRetriever;
import com.nytimes.dv.slack.extractor.service.SlackUserInfoRetriever;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.stream.Collectors;

@SpringBootApplication
@EnableCaching
@Slf4j
public class SlackMessageExtractorApplication {

    private static final String SLACK_API = "https://slack.com/api";

    public static void main(String[] args) {
        SpringApplication.run(SlackMessageExtractorApplication.class, args);
    }

    @Bean
    WebClient webClient() {
        return WebClient.builder().baseUrl(SLACK_API).build();
    }

    @Bean
    public CommandLineRunner runner(@Value("${slack.messages.oldest_in_days}") Integer fromDays,
                                    SlackMessageRetriever slackMessageRetriever,
                                    SlackUserInfoRetriever userInfoRetriever,
                                    Elasticsearch elasticsearch) {
        return args -> {
            SlackMessageResult result = slackMessageRetriever.list(fromDays);
            do {
                elasticsearch.saveAll(result.getMessages()
                        .stream()
                        .parallel()
                        // you may add filters here for data clean-up or remove these filters
                        .filter(m -> !m.getText().contains("Reminder:"))
                        .filter(m -> !m.getText().matches("^:.*:$"))
                        .map(m -> m.withUserInfo(userInfoRetriever.get(m.getUser())))
                        .map(SlackMessage::loadTags)
                        .collect(Collectors.toList())
                );
                log.info("Loading new batch - cursor: {}", result.getNextCursor());
                result = slackMessageRetriever.list(result.getNextCursor(), fromDays);
                result = slackMessageRetriever.list(result.getNextCursor(), fromDays);
            } while (result.getMore());
        };
    }
}
