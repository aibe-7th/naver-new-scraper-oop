package oop.scraper.application;

import oop.scraper.domain.NewsResult;

import java.util.List;

public interface NewsProvider {
    List<NewsResult> fetchNews(String searchQuery, int limit);
}
