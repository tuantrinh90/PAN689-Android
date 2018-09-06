package com.football.fantasy.fragments.leagues.your_team.player_list.transfer;

import android.support.annotation.NonNull;

import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;

public class PlayerListTransferFragment extends PlayerListFragment<IPlayerListTransferView, IPlayerListTransferPresenter<IPlayerListTransferView>> implements IPlayerListTransferView {

    @NonNull
    @Override
    public IPlayerListTransferPresenter<IPlayerListTransferView> createPresenter() {
        return new PlayerListTransferPresenter(getAppComponent());
    }

    @Override
    protected void getPlayers(int leagueId, boolean sortDesc, int page, String query, String filterPositions, String filterClubs) {
        presenter.getPlayers(leagueId, sortDesc, page, query, filterPositions, filterClubs);
    }
}
