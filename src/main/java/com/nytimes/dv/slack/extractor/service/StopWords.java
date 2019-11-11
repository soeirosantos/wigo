package com.nytimes.dv.slack.extractor.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class StopWords {

    private static Set<String> words;

    static {
        try {
            words = new HashSet<>(Files.readAllLines(Path.of("src/main/resources/stopwords.txt")));
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static boolean isNot(String w) {
        return !words.contains(w);
    }
}
