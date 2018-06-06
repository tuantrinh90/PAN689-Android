package com.football.models.responses;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class FormResponse {
    @JsonProperty("budget_options")
    List<BudgetResponse> budgets;

    public List<BudgetResponse> getBudgets() {
        return budgets;
    }

    public void setBudgets(List<BudgetResponse> budgets) {
        this.budgets = budgets;
    }

    @Override
    public String toString() {
        return "FormResponse{" +
                "budgets=" + budgets +
                '}';
    }
}
