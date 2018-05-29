package com.football.models.responses;

import java.io.Serializable;

public class League implements Serializable{
    public static final int OPEN_LEAGUES = 1;
    public static final int MY_LEAGUES = 2;
    public static final int PENDING_LEAGUES = 3;

    public static final int TYPE_INCREASE = 1;
    public static final int TYPE_DECREASE = 2;

    private String avatar;
    private String title;
    private String owner;
    private String invitor;
    private int rankNumber;
    private int rankTotal;
    private String description;
    private int status;
    private int rate;
    private int type;

    public League() {
    }

    public League(String avatar, String title, String owner, String invitor,
                  int rankNumber, int rankTotal, String description, int status, int rate) {
        this.avatar = avatar;
        this.title = title;
        this.owner = owner;
        this.invitor = invitor;
        this.rankNumber = rankNumber;
        this.rankTotal = rankTotal;
        this.description = description;
        this.status = status;
        this.rate = rate;
    }

    public League(String avatar, String title, String owner, String invitor, int rankNumber,
                  int rankTotal, String description, int status, int rate, int type) {
        this.avatar = avatar;
        this.title = title;
        this.owner = owner;
        this.invitor = invitor;
        this.rankNumber = rankNumber;
        this.rankTotal = rankTotal;
        this.description = description;
        this.status = status;
        this.rate = rate;
        this.type = type;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getInvitor() {
        return invitor;
    }

    public void setInvitor(String invitor) {
        this.invitor = invitor;
    }

    public int getRankNumber() {
        return rankNumber;
    }

    public void setRankNumber(int rankNumber) {
        this.rankNumber = rankNumber;
    }

    public int getRankTotal() {
        return rankTotal;
    }

    public void setRankTotal(int rankTotal) {
        this.rankTotal = rankTotal;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "League{" +
                "avatar='" + avatar + '\'' +
                ", title='" + title + '\'' +
                ", owner='" + owner + '\'' +
                ", invitor='" + invitor + '\'' +
                ", rankNumber=" + rankNumber +
                ", rankTotal=" + rankTotal +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", rate=" + rate +
                ", type=" + type +
                '}';
    }
}
