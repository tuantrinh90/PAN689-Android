package com.football.fantasy.fragments.leagues.your_team.player_list.transfer;

import android.support.annotation.NonNull;
import android.view.View;

import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICK;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICKED;

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

    @Override
    protected void onDetailPlayerClicked(PlayerResponse player) {
        PlayerDetailForLineupFragment.start(
                this,
                player,
                -1,
                getString(R.string.lineup),
                league.getGameplayOption(),
                player.getSelected() ? PICK_PICKED : PICK_PICK,
                playerPosition == PlayerResponse.POSITION_NONE ? player.getMainPosition() : playerPosition,
                NONE_ORDER);
    }
}
