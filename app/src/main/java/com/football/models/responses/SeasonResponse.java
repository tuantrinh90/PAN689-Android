package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public class SeasonResponse implements Serializable {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
