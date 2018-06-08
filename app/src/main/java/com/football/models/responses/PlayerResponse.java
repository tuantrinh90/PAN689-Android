package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PlayerResponse implements Serializable {

    public static final int POSITION_GOALKEEPER = 0;

    public static final int POSITION_DEFENDER = 1;

    public static final int POSITION_MIDFIELDER = 2;

    public static final int POSITION_ATTACKER = 3;

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("real_club_id")
    private Integer realClubId;
    @JsonProperty("real_club")
    private RealClub realClub;
    @JsonProperty("name")
    private String name;
    @JsonProperty("nickname")
    private String nickname;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("is_injured")
    private Boolean isInjured;
    @JsonProperty("is_goalkeeper")
    private Boolean isGoalkeeper;
    @JsonProperty("is_defender")
    private Boolean isDefender;
    @JsonProperty("is_midfielder")
    private Boolean isMidfielder;
    @JsonProperty("is_attacker")
    private Boolean isAttacker;
    @JsonProperty("main_position")
    private Integer mainPosition;
    @JsonProperty("minor_position")
    private Integer minorPosition;
    @JsonProperty("transfer_value")
    private long transferValue;

    public Integer getId() {
        return id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Integer getRealClubId() {
        return realClubId;
    }

    public RealClub getRealClub() {
        return realClub;
    }

    public String getName() {
        return name;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPhoto() {
        return photo;
    }

    public Boolean getInjured() {
        return isInjured;
    }

    public Boolean getGoalkeeper() {
        return isGoalkeeper;
    }

    public Boolean getDefender() {
        return isDefender;
    }

    public Boolean getMidfielder() {
        return isMidfielder;
    }

    public Boolean getAttacker() {
        return isAttacker;
    }

    public Integer getMainPosition() {
        return mainPosition;
    }

    public Integer getMinorPosition() {
        return minorPosition;
    }

    public long getTransferValue() {
        return transferValue;
    }

    public String getTransferStringValue() {
        return String.valueOf(transferValue / 1000000f);
    }

    public String getMainPositionString() {
        return getPositionString(mainPosition);
    }

    public String getMinorPositionString() {
        return getPositionString(minorPosition);
    }

    private String getPositionString(Integer position) {
        if (position != null) {
            switch (position) {
                case POSITION_GOALKEEPER:
                    return "G";
                case POSITION_DEFENDER:
                    return "D";
                case POSITION_MIDFIELDER:
                    return "M";
                case POSITION_ATTACKER:
                    return "A";
            }
        }
        return null;
    }

    public PlayerResponse() {
    }

    public static class RealClub {

        @JsonProperty("id")
        private Integer id;
        @JsonProperty("name")
        private String name;

        public RealClub() {
        }

        public Integer getId() {
            return id;
        }

        public String getName() {
            return name;
        }
    }
}
