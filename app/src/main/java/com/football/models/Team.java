package com.football.models;

import java.io.Serializable;

public class Team implements Serializable {
    private String avatar;
    private String teamName;
    private String description;
    private boolean isLock;
    private boolean isChecked;

    public Team() {
    }

    public Team(String avatar, String teamName, String description, boolean isLock) {
        this.avatar = avatar;
        this.teamName = teamName;
        this.description = description;
        this.isLock = isLock;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean getIsLock() {
        return isLock;
    }

    public void setIsLock(boolean isLock) {
        this.isLock = isLock;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    @Override
    public String toString() {
        return "Team{" +
                "avatar='" + avatar + '\'' +
                ", teamName='" + teamName + '\'' +
                ", description='" + description + '\'' +
                ", isLock=" + isLock +
                ", isChecked=" + isChecked +
                '}';
    }
}
