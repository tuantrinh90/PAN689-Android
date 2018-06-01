package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FormResponse {

    @JsonProperty("budget_options")
    public List<BudgetResponse> budgets;

}
