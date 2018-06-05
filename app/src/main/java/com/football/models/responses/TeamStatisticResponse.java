package com.football.models.responses;

import java.io.Serializable;

public class TeamStatisticResponse implements Serializable {
    String round;
    String point;
    String change;
    boolean isIncrease;

    public TeamStatisticResponse() {
    }

    public TeamStatisticResponse(String round, String point, String change, boolean isIncrease) {
        this.round = round;
        this.point = point;
        this.change = change;
        this.isIncrease = isIncrease;
    }

    public String getRound() {
        return round;
    }

    public void setRound(String round) {
        this.round = round;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public boolean isIncrease() {
        return isIncrease;
    }

    public void setIncrease(boolean increase) {
        isIncrease = increase;
    }

    @Override
    public String toString() {
        return "TeamStatisticResponse{" +
                "round='" + round + '\'' +
                ", point='" + point + '\'' +
                ", change='" + change + '\'' +
                ", isIncrease=" + isIncrease +
                '}';
    }
}
