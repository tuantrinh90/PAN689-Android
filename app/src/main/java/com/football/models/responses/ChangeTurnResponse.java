package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class ChangeTurnResponse implements Serializable {

    @JsonProperty("previous")
    private TurnResponse previous;
    @JsonProperty("current")
    private TurnResponse current;
    @JsonProperty("next")
    private TurnResponse next;

    public TurnResponse getPrevious() {
        return previous;
    }

    public TurnResponse getCurrent() {
        return current;
    }

    public TurnResponse getNext() {
        return next;
    }
}
