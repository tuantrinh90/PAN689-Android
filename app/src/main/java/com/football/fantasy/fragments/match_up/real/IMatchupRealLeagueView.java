package com.football.fantasy.fragments.match_up.real;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.RealMatch;

import java.util.List;

public interface IMatchupRealLeagueView extends IBaseMvpView {
    void displayRealMatches(List<RealMatch> realMatches);

    void stopLoading();
}
