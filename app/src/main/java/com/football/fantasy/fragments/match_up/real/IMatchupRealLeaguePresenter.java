package com.football.fantasy.fragments.match_up.real;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IMatchupRealLeaguePresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getMaxRound();

    void getRealMatches(String round, int page);

}
