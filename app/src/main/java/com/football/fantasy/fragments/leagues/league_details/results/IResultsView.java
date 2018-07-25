package com.football.fantasy.fragments.leagues.league_details.results;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.MatchResponse;

import java.util.List;

public interface IResultsView extends IBaseMvpView {
    void displayRound(Integer round);

    void displayTime(String startAt);

    void displayMatches(List<MatchResponse> matches);

}
