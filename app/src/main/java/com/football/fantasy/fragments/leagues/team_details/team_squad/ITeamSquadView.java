package com.football.fantasy.fragments.leagues.team_details.team_squad;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamSquadResponse;

public interface ITeamSquadView extends IBaseMvpView {
    void displayViews(String gameplayOption);

    void displayTeamSquad(TeamSquadResponse response);

    void handleAction();
}
