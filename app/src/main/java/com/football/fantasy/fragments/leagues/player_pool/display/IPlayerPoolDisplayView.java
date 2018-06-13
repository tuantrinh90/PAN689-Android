package com.football.fantasy.fragments.leagues.player_pool.display;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.RealClubResponse;

import java.util.List;

public interface IPlayerPoolDisplayView extends IBaseMvpView {

    void displayClubs(List<RealClubResponse> clubs);

}
