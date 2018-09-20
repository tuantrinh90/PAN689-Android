package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DraftCountdownResponse implements Serializable {
    @JsonProperty("id")
    private int id;
    @JsonProperty("number_of_user")
    private int numberOfUser;
    @JsonProperty("current_number_of_user")
    private int currentNumberOfUser;
    @JsonProperty("draft_total_joined")
    private int draftTotalJoined;
    @JsonProperty("current_time")
    private String currentTime;
    @JsonProperty("draft_time_left")
    private int draftTimeLeft;

    public int getId() {
        return id;
    }

    public int getNumberOfUser() {
        return numberOfUser;
    }

    public int getCurrentNumberOfUser() {
        return currentNumberOfUser;
    }

    public int getDraftTotalJoined() {
        return draftTotalJoined;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public int getDraftTimeLeft() {
        return draftTimeLeft;
    }
}
