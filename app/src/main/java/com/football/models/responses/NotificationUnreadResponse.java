package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class NotificationUnreadResponse implements Serializable {

    @JsonProperty("total")
    private int total;

    public int getTotal() {
        return total;
    }
}
