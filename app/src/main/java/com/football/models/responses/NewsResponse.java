package com.football.models.responses;

import com.bon.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.Constant;

import java.io.Serializable;
import java.util.Calendar;

public class NewsResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("created_at")
    private String createdAt;
    @JsonProperty("updated_at")
    private String updatedAt;
    @JsonProperty("category_id")
    private Object categoryId;
    @JsonProperty("category")
    private Object category;
    @JsonProperty("title")
    private String title;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("description")
    private String description;
    @JsonProperty("publish_on")
    private String publishOn;
    @JsonProperty("author")
    private String author;
    @JsonProperty("user_id")
    private Integer userId;
    @JsonProperty("user")
    private UserResponse user;
    @JsonProperty("url")
    private String url;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Object getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Object categoryId) {
        this.categoryId = categoryId;
    }

    public Object getCategory() {
        return category;
    }

    public void setCategory(Object category) {
        this.category = category;
    }

    public String getTitle() {
        return title + "";
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getThumbnail() {
        return thumbnail + "";
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description + "";
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublishOn() {
        return publishOn;
    }

    public void setPublishOn(String publishOn) {
        this.publishOn = publishOn;
    }

    @JsonIgnore
    public Calendar getPublishOnCalendar() {
        return DateTimeUtils.convertStringToCalendar(publishOn, Constant.FORMAT_DATE_TIME_SERVER);
    }

    @JsonIgnore
    public String getDay() {
        Calendar calendar = getPublishOnCalendar();
        if (calendar == null) return "";

        return DateTimeUtils.convertCalendarToString(calendar, "dd");
    }

    @JsonIgnore
    public String getMonth() {
        Calendar calendar = getPublishOnCalendar();
        if (calendar == null) return "";

        return DateTimeUtils.convertCalendarToString(calendar, "MM");
    }

    @JsonIgnore
    public String getYear() {
        Calendar calendar = getPublishOnCalendar();
        if (calendar == null) return "";

        return DateTimeUtils.convertCalendarToString(calendar, "yyyy");
    }


    public String getAuthor() {
        return author + "";
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewsResponse{" +
                "id=" + id +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                ", categoryId=" + categoryId +
                ", category=" + category +
                ", title='" + title + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                ", publishOn='" + publishOn + '\'' +
                ", author='" + author + '\'' +
                ", userId=" + userId +
                ", user=" + user +
                ", url='" + url + '\'' +
                '}';
    }
}
