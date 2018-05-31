package com.football.fantasy.fragments.leagues.my_leagues;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

import java.util.List;

public interface IMyLeagueView extends IBaseMvpView {
    void notifyDataSetChangedLeagues(List<LeagueResponse> its);
}
