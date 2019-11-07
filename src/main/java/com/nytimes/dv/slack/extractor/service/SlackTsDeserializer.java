package com.nytimes.dv.slack.extractor.service;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

public class SlackTsDeserializer extends JsonDeserializer<LocalDateTime> {

    @Override
    public LocalDateTime deserialize(JsonParser jsonParser,
                                     DeserializationContext deserializationContext) throws IOException {
        final String ts = jsonParser.getText();
        return tsToLocalDateTime(ts).orElse(null);
    }

    private Optional<LocalDateTime> tsToLocalDateTime(String ts) {
        final String[] split = ts.split("\\.");
        if (split.length > 0) {
            final long timeAsLong = Long.parseLong(split[0]);
            return Optional.of(LocalDateTime.ofInstant(Instant.ofEpochSecond(timeAsLong), ZoneId.systemDefault()));
        }
        return Optional.empty();
    }
}
