package com.football.fantasy.fragments.leagues.your_team.player_list.transfer;

import android.support.annotation.NonNull;
import android.view.View;

import com.football.events.PlayerEvent;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;

public class PlayerListTransferFragment extends PlayerListFragment<IPlayerListTransferView, IPlayerListTransferPresenter<IPlayerListTransferView>> implements IPlayerListTransferView {

    @NonNull
    @Override
    public IPlayerListTransferPresenter<IPlayerListTransferView> createPresenter() {
        return new PlayerListTransferPresenter(getAppComponent());
    }

    @Override
    protected void initView() {
        super.initView();
        // đang ở setupTime && chưa completed
        boolean visibleAddButton = AppUtilities.isSetupTime(league);
        playerAdapter.setVisibleAddButton(visibleAddButton ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void getPlayers(int leagueId, boolean sortDesc, int page, String query, String filterPositions, String filterClubs) {
        presenter.getPlayers(leagueId, sortDesc, page, query, filterPositions, filterClubs);
    }

    @Override
    protected void onAddPlayerClicked(PlayerResponse player, Integer position) {
        showLoading(true);
        // bắn sang màn hình LineUp
        bus.send(new PlayerEvent.Builder()
                .action(PlayerEvent.ACTION_ADD_CLICK)
                .position(playerPosition == PlayerResponse.POSITION_NONE ? player.getMainPosition() : playerPosition)
                .data(player)
                .callback((boo, error) -> {
                    showLoading(false);
                    if (boo) {
                        player.setSelected(true);
                        playerAdapter.notifyItemChanged(position);
                    } else {
                        showMessage(error);
                    }
                })
                .build());
    }
}
