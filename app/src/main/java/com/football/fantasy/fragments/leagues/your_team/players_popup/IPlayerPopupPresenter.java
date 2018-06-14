package com.football.fantasy.fragments.leagues.your_team.players_popup;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IPlayerPopupPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getPlayers(int leagueId, String orderBy, int page, int numberPerPage, String query, Integer mainPosition);
}
