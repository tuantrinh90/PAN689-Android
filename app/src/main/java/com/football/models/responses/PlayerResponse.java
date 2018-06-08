package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class PlayerResponse implements Serializable {

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
    private Integer transferValue;

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

    public Integer getTransferValue() {
        return transferValue;
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
