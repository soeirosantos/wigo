package com.nytimes.dv.slack.extractor.repository;

import com.nytimes.dv.slack.extractor.entity.SlackMessage;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Elasticsearch extends ElasticsearchRepository<SlackMessage, String> {
}
