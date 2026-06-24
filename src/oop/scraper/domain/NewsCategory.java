package oop.scraper.domain;

public enum NewsCategory {
    SIM("sim", "정확도순"),
    DATE("date", "최신순");

    private final String queryValue;
    private final String description;

    NewsCategory(String queryValue, String description) {
        this.queryValue = queryValue;
        this.description = description;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public String getDescription() {
        return description;
    }
}
