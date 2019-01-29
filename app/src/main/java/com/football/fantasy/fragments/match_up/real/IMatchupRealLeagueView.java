package com.football.fantasy.fragments.match_up.real;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.RealMatch;

import java.util.List;

public interface IMatchupRealLeagueView extends IBaseMvpView {

    void displayMaxRound(Integer total);

    void displayRealMatches(List<RealMatch> realMatches);

    void stopLoading();

    void displayRound(Integer round);

}
