package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NotificationResponse implements Serializable {

    public static final String TYPE_INFO = "info";
    public static final String TYPE_ACCEPT = "accept";
    public static final String TYPE_REJECT = "reject";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_LEAGUE = "league";
    public static final String TYPE_WARNING = "warning";

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("notification_time")
    private String notificationTime;
    @JsonProperty("key")
    private Key key;
    @JsonProperty("title")
    private String title;
    @JsonProperty("type")
    private String type;

    public Integer getId() {
        return id;
    }

    public String getNotificationTime() {
        return notificationTime == null ? "" : notificationTime;
    }

    public String getNotificationDate() {
        if (notificationTime != null && notificationTime.length() > 0) {
            if (notificationTime.contains(" ")) {
                return notificationTime.substring(0, notificationTime.indexOf(" "));
            }
            return notificationTime;
        }
        return "";
    }

    public Key getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    public static class Key implements Serializable {

        @JsonProperty("league_id")
        private Integer leagueId;
        @JsonProperty("action")
        private String action;

        public Integer getLeagueId() {
            return leagueId;
        }

        public String getAction() {
            return action;
        }
    }
}
