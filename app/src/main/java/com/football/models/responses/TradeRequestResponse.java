package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.List;

public class TradeRequestResponse implements Serializable {
    @JsonProperty("from")
    private List<TradeResponse> from;
    @JsonProperty("to")
    private List<TradeResponse> to;

    public List<TradeResponse> getFrom() {
        return from;
    }

    public List<TradeResponse> getTo() {
        return to;
    }
}
