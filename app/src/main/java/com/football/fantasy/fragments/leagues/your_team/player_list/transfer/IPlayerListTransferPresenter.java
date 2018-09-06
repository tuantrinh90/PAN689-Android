package com.football.fantasy.fragments.leagues.your_team.player_list.transfer;

import com.football.fantasy.fragments.leagues.your_team.player_list.IPlayerListPresenter;
import com.football.fantasy.fragments.leagues.your_team.player_list.IPlayerListView;

public interface IPlayerListTransferPresenter<V extends IPlayerListView> extends IPlayerListPresenter<V> {

    void getPlayers(int leagueId, boolean valueSortDesc, int page, String query,
                    String filterPositions, String filterClubs);

}
