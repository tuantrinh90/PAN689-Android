package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NotificationResponse implements Serializable {

    public static final String TYPE_INFO = "info";
    public static final String TYPE_ACCEPT = "accept";
    public static final String TYPE_REJECT = "reject";
    public static final String TYPE_EMAIL = "email";
    public static final String TYPE_LEAGUE = "league";
    public static final String TYPE_LEAGUE_START = "league_start";
    public static final String TYPE_LEAGUE_FINISH = "league_finish";
    public static final String TYPE_WARNING = "warning";


    @JsonProperty("id")
    private int id;
    @JsonProperty("notification_time")
    private String notificationTime;
    @JsonProperty("key")
    private Key key;
    @JsonProperty("title")
    private String title;
    @JsonProperty("type")
    private String type;

    public int getId() {
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

        @JsonProperty("action")
        private String action;
        @JsonProperty("league_id")
        private int leagueId;
        @JsonProperty("league_status")
        private int leagueStatus;
        @JsonProperty("team_name")
        private String teamName;
        @JsonProperty("my_team_id")
        private int myTeamId;
        @JsonProperty("team_id")
        private int teamId;
        @JsonProperty("player_id")
        private int playerId;
        @JsonProperty("trade_id")
        private int tradeId;

        public int getLeagueId() {
            return leagueId;
        }

        public int getLeagueStatus() {
            return leagueStatus;
        }

        public String getAction() {
            return action;
        }

        public String getTeamName() {
            return teamName;
        }

        public int getMyTeamId() {
            return myTeamId;
        }

        public int getTeamId() {
            return teamId;
        }

        public int getPlayerId() {
            return playerId;
        }

        public int getTradeId() {
            return tradeId;
        }
    }
}
