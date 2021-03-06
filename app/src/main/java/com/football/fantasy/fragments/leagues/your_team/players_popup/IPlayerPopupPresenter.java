package com.football.fantasy.fragments.leagues.your_team.players_popup;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IPlayerPopupPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getPlayers(int seasonId, int leagueId, int valueDirection, int page, String query, Integer mainPosition, String filterClubs);
}
