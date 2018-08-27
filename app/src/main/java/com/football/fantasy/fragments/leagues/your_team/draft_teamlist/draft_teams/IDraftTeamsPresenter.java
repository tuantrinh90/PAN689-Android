package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_teams;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IDraftTeamsPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getTeams(int id);
}
