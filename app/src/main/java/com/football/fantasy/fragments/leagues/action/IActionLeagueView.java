package com.football.fantasy.fragments.leagues.action;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.BudgetResponse;

import java.util.List;

public interface IActionLeagueView extends IBaseMvpView {

    void displayBudgets(List<BudgetResponse> budgets);

    void openCreateTeam();

    void updateSuccess();

}
