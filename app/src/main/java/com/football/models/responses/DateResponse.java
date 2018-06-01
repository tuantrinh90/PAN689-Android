package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class DateResponse implements Serializable {
    @JsonProperty("date")
    private String date;
    @JsonProperty("timezone_type")
    private Integer timezoneType;
    @JsonProperty("timezone")
    private String timezone;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getTimezoneType() {
        return timezoneType;
    }

    public void setTimezoneType(Integer timezoneType) {
        this.timezoneType = timezoneType;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public String toString() {
        return "DateResponse{" +
                "date='" + date + '\'' +
                ", timezoneType=" + timezoneType +
                ", timezone='" + timezone + '\'' +
                '}';
    }
}
