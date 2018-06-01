package com.football.fantasy.fragments.leagues.league_details.successor;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.TeamResponse;

import java.util.List;

public interface ISuccessorView extends IBaseMvpView{
    void displayTeams(List<TeamResponse> teams);
    void onLeaveLeague();
}
