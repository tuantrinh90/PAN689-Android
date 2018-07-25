package com.football.fantasy.fragments.match_up.my;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.MatchResponse;

import java.util.List;

public interface IMatchupMyLeagueView extends IBaseMvpView {
    void displayMatches(List<MatchResponse> matches);

    void stopLoading();
}
