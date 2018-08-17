package com.football.models.responses;

import com.bon.util.DateTimeUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.football.utilities.Constant;

import java.io.Serializable;
import java.util.Calendar;

public class TeamRoundResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("round")
    private Integer round;
    @JsonProperty("start_at")
    private String startAt;
    @JsonProperty("end_at")
    private String endAt;
    @JsonProperty("point")
    private Integer point;
    @JsonProperty("formation")
    private String formation;
    @JsonProperty("transfer_deadline")
    private String transferDeadline;

    public Integer getId() {
        return id;
    }

    public Integer getRound() {
        return round;
    }

    public String getStartAt() {
        return startAt;
    }

    public String getEndAt() {
        return endAt;
    }

    public Integer getPoint() {
        return point;
    }

    public String getFormation() {
        return formation;
    }

    public String getTransferDeadline() {
        return transferDeadline;
    }

    @JsonIgnore
    public Calendar getTransferDeadlineCalendar() {
        return DateTimeUtils.convertStringToCalendar(transferDeadline, Constant.FORMAT_DATE_TIME_SERVER);
    }

    @JsonIgnore
    public Calendar getEndAtCalendar() {
        return DateTimeUtils.convertStringToCalendar(endAt, Constant.FORMAT_DATE_TIME_SERVER);
    }
}
