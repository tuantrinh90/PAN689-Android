package com.football.models.responses;

import java.io.Serializable;

public class PlayerResponse implements Serializable {
    String avatar;
    String name;
    String value;
    String point;
    String positionPrimary;
    String positionSecond;
    String status;
    String statOne;

    public PlayerResponse() {
    }

    public PlayerResponse(String avatar, String name, String value, String point,
                          String positionPrimary, String positionSecond,
                          String status, String statOne) {
        this.avatar = avatar;
        this.name = name;
        this.value = value;
        this.point = point;
        this.positionPrimary = positionPrimary;
        this.positionSecond = positionSecond;
        this.status = status;
        this.statOne = statOne;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getPoint() {
        return point;
    }

    public void setPoint(String point) {
        this.point = point;
    }

    public String getPositionPrimary() {
        return positionPrimary;
    }

    public void setPositionPrimary(String positionPrimary) {
        this.positionPrimary = positionPrimary;
    }

    public String getPositionSecond() {
        return positionSecond;
    }

    public void setPositionSecond(String positionSecond) {
        this.positionSecond = positionSecond;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatOne() {
        return statOne;
    }

    public void setStatOne(String statOne) {
        this.statOne = statOne;
    }

    @Override
    public String toString() {
        return "PlayerResponse{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", value='" + value + '\'' +
                ", point='" + point + '\'' +
                ", positionPrimary='" + positionPrimary + '\'' +
                ", positionSecond='" + positionSecond + '\'' +
                ", status='" + status + '\'' +
                ", statOne='" + statOne + '\'' +
                '}';
    }

}
