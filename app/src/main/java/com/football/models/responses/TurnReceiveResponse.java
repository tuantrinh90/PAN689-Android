package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TurnReceiveResponse implements Serializable {
    @JsonProperty("event")
    private String event;
    @JsonProperty("root_number")
    private Integer rootNumber;
    @JsonProperty("number")
    private Integer number;
    @JsonProperty("room")
    private String room;
    @JsonProperty("total_player")
    private Integer totalPlayer;
    @JsonProperty("total_round")
    private Integer totalRound;
    @JsonProperty("pick_round")
    private Integer pickRound;
    @JsonProperty("show_pick_round")
    private Integer showPickRound;
    @JsonProperty("league_id")
    private Integer leagueId;
    @JsonProperty("league")
    private List<TurnResponse> leagues;
    @JsonProperty("lineup")
    private Object lineup;

    public String getEvent() {
        return event;
    }

    public Integer getRootNumber() {
        return rootNumber;
    }

    public Integer getNumber() {
        return number;
    }

    public String getRoom() {
        return room;
    }

    public Integer getTotalPlayer() {
        return totalPlayer;
    }

    public Integer getTotalRound() {
        return totalRound;
    }

    public Integer getPickRound() {
        return pickRound;
    }

    public Integer getShowPickRound() {
        return showPickRound;
    }

    public Integer getLeagueId() {
        return leagueId;
    }

    public List<TurnResponse> getLeagues() {
        return leagues;
    }

    public Object getLineup() {
        return lineup;
    }
}
