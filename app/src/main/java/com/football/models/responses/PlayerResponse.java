package com.football.models.responses;

import java.io.Serializable;

public class PlayerResponse implements Serializable {
    String avatar;
    String name;
    String point;
    String positionPrimary;
    String positionSecond;
    String status;

    public PlayerResponse() {
    }

    public PlayerResponse(String avatar, String name, String point, String positionPrimary,
                          String positionSecond, String status) {
        this.avatar = avatar;
        this.name = name;
        this.point = point;
        this.positionPrimary = positionPrimary;
        this.positionSecond = positionSecond;
        this.status = status;
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

    @Override
    public String toString() {
        return "PlayerResponse{" +
                "avatar='" + avatar + '\'' +
                ", name='" + name + '\'' +
                ", point='" + point + '\'' +
                ", positionPrimary='" + positionPrimary + '\'' +
                ", positionSecond='" + positionSecond + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
