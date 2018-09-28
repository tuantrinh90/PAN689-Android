package com.football.fantasy.fragments.leagues.your_team.player_list.draft;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.SocketEventKey;

import butterknife.BindView;

public class PlayerListDraftFragment extends PlayerListFragment<IPlayerListDraftView, IPlayerListDraftPresenter<IPlayerListDraftView>> implements IPlayerListDraftView {

    @BindView(R.id.sortValue)
    View sortValue;

    @Override
    protected void initView() {
        super.initView();

        playerAdapter.setVisibleValue(View.GONE);
        sortValue.setVisibility(View.GONE);
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

    @Override
    protected void onAddPlayerClicked(PlayerResponse player, Integer position) {
        // bắn sang màn hình LineUp
        bus.send(new PlayerEvent.Builder()
                .action(PlayerEvent.ACTION_ADD_CLICK)
                .position(playerPosition == PlayerResponse.POSITION_NONE ? player.getMainPosition() : playerPosition)
                .data(player)
                .callback((boo, error) -> {
                    if (boo) {
                        player.setSelected(true);
                        playerAdapter.notifyItemChanged(position);
                    } else {
                        showMessage(error);
                    }
                })
                .build());
    }

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_ADD_PLAYER, args -> {
            Log.i(SocketEventKey.EVENT_ADD_PLAYER, "registerSocket: ");
            mActivity.runOnUiThread(this::refresh);
        });
    }
}
