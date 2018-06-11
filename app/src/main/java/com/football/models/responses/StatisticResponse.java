package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class StatisticResponse implements Serializable {
    @JsonProperty("goalkeeper")
    private Integer goalkeeper;
    @JsonProperty("defender")
    private Integer defender;
    @JsonProperty("midfielder")
    private Integer midfielder;
    @JsonProperty("attacker")
    private Integer attacker;

    public Integer getGoalkeeper() {
        return goalkeeper;
    }

    public void setGoalkeeper(Integer goalkeeper) {
        this.goalkeeper = goalkeeper;
    }

    public Integer getDefender() {
        return defender;
    }

    public void setDefender(Integer defender) {
        this.defender = defender;
    }

    public Integer getMidfielder() {
        return midfielder;
    }

    public void setMidfielder(Integer midfielder) {
        this.midfielder = midfielder;
    }

    public Integer getAttacker() {
        return attacker;
    }

    public void setAttacker(Integer attacker) {
        this.attacker = attacker;
    }
}
