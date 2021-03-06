package com.football.fantasy.fragments.leagues.action.setup_leagues;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.BudgetResponse;
import com.football.models.responses.LeagueResponse;

import java.util.List;

public interface ISetupLeagueView extends IBaseMvpView {
    void displayBudgets(List<BudgetResponse> budgets);

    void openCreateTeam(LeagueResponse leagueResponse);

    void updateSuccess(LeagueResponse league);
}
