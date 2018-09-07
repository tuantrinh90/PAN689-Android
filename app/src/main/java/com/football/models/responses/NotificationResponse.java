package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NotificationResponse implements Serializable {

    @JsonProperty("title")
    private String title;
    @JsonProperty("title_html")
    private String titleHtml;
    @JsonProperty("created_at")
    private String createdAt;

    public String getTitle() {
        return title;
    }

    public String getTitleHtml() {
        return titleHtml;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getCreatedDate() {
        if (createdAt != null && createdAt.length() > 0) {
            if (createdAt.contains(" ")) {
                return createdAt.substring(0, createdAt.indexOf(" "));
            }
            return createdAt;
        }
        return "";
    }
}
