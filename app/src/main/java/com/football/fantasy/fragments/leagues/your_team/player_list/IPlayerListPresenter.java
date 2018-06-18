package com.football.fantasy.fragments.leagues.your_team.player_list;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IPlayerListPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void getPlayers(int leagueId, boolean valueSortDesc, int page, int numberPerPage, String query, Integer mainPosition, boolean newPlayers);

}
