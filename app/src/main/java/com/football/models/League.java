package com.football.models;

public class League {
    private String avatar;
    private String title;
    private String description;
    private int rankNumber;
    private int rankTotal;

    public League() {
    }

    public League(String avatar, String title, String description, int rankNumber, int rankTotal) {
        this.avatar = avatar;
        this.title = title;
        this.description = description;
        this.rankNumber = rankNumber;
        this.rankTotal = rankTotal;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @Override
    public String toString() {
        return "League{" +
                "avatar='" + avatar + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", rankNumber=" + rankNumber +
                ", rankTotal=" + rankTotal +
                '}';
    }
}
