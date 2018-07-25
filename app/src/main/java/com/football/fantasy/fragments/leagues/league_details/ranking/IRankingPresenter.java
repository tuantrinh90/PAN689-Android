package com.football.fantasy.fragments.leagues.league_details.ranking;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IRankingPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getRanking(Integer leagueId);
}
