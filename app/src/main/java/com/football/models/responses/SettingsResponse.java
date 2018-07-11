package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SettingsResponse implements Serializable {

    @JsonProperty("id")
    private Integer id;
    @JsonProperty("allow_notification")
    private Boolean allowNotification;
    @JsonProperty("allow_email_alert")
    private Boolean allowEmailAlert;

    public Integer getId() {
        return id;
    }

    public Boolean getAllowNotification() {
        return allowNotification;
    }

    public Boolean getAllowEmailAlert() {
        return allowEmailAlert;
    }
}
