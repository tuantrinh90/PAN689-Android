package com.football.fantasy.fragments.leagues.team_squad.trade.choose_a_team;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IChooseATeamPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getTeams(int leagueId, int teamId);
}
