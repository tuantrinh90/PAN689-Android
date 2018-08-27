package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_picks;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IDraftPicksPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getPickHistories(int leagueId, int page);
}
