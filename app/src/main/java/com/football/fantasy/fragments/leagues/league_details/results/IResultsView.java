package com.football.fantasy.fragments.leagues.league_details.results;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.MatchResponse;

import java.util.List;

public interface IResultsView extends IBaseMvpView {
    void displayMatches(List<MatchResponse> matches);

    void displayRound(int round);

    void displayTotalRound(int totalRound);

}
