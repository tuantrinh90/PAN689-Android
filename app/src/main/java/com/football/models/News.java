package com.football.models;

public class News {
    private String day;
    private String month;
    private String content;

    public News() {
    }

    public News(String day, String month, String content) {
        this.day = day;
        this.month = month;
        this.content = content;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "News{" +
                "day='" + day + '\'' +
                ", month='" + month + '\'' +
                ", content='" + content + '\'' +
                '}';
    }
}
