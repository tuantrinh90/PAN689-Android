package com.football.fantasy.fragments.leagues.player_pool.filter;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.RealClubResponse;

import java.util.List;

public interface IPlayerPoolFilterView extends IBaseMvpView {
    void displayClubs(List<RealClubResponse> data);
}
