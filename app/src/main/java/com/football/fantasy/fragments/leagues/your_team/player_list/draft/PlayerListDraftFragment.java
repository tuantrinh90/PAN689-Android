package com.football.fantasy.fragments.leagues.your_team.player_list.draft;

import android.support.annotation.NonNull;
import android.util.Log;

import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.utilities.SocketEventKey;

public class PlayerListDraftFragment extends PlayerListFragment<IPlayerListDraftView, IPlayerListDraftPresenter<IPlayerListDraftView>> implements IPlayerListDraftView {

    @Override
    protected void initialized() {
        super.initialized();

        registerSocket();
    }

    @Override
    public void onDestroyView() {
        getAppContext().off(SocketEventKey.EVENT_ADD_PLAYER);
        super.onDestroyView();
    }

    @NonNull
    @Override
    public IPlayerListDraftPresenter<IPlayerListDraftView> createPresenter() {
        return new PlayerListDraftPresenter(getAppComponent());
    }

    @Override
    protected void getPlayers(int leagueId, boolean sortDesc, int page, String query, String filterPositions, String filterClubs) {
        presenter.getPlayers(leagueId, sortDesc, page, query, filterPositions, filterClubs);
    }

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_ADD_PLAYER, args -> {
            Log.i(SocketEventKey.EVENT_ADD_PLAYER, "registerSocket: ");
        });
    }
}
