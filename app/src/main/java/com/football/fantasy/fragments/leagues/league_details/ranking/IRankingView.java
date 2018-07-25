package com.football.fantasy.fragments.leagues.league_details.ranking;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.RankingResponse;

import java.util.List;

public interface IRankingView extends IBaseMvpView {
    void displayRanking(List<RankingResponse> rankingList);
}
