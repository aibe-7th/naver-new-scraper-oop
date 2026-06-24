package oop.scraper.application;

import oop.scraper.domain.NewsResult;

import java.util.List;

public interface NewsPublisher {
    void publish(String topic, List<NewsResult> results);
}
