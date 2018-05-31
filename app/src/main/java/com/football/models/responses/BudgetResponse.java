package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BudgetResponse {
    @JsonProperty("id")
    private Integer id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("value")
    private Integer value;

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getValue() {
        return value;
    }
}
