package com.football.models.responses;

import java.io.Serializable;
import java.util.List;

public class RealMatch implements Serializable {

    private String date;
    private List<RealMatchResponse> responses;

    public RealMatch(String date, List<RealMatchResponse> responses) {
        this.date = date;
        this.responses = responses;
    }

    public String getDate() {
        return date;
    }

    public List<RealMatchResponse> getResponses() {
        return responses;
    }
}
