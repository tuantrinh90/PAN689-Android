package com.football.fantasy.fragments.leagues.action.setup_teams;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

public interface ISetupTeamView extends IBaseMvpView {

    void createTeamSuccess();

    void updateTeamSuccess(TeamResponse response);

}
