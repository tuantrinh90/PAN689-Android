package com.football.fantasy.fragments.leagues.your_team.player_list.draft;

import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.football.events.GeneralEvent;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.SocketEventKey;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_NONE;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICK;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICKED;

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
    protected void registerBus() {
        super.registerBus();
        mCompositeDisposable.add(bus.ofType(GeneralEvent.class)
                .subscribeWith(new DisposableObserver<GeneralEvent>() {
                    @Override
                    public void onNext(GeneralEvent event) {
                        switch (event.getSource()) {
                            case LINEUP_DRAFT:
                                visibleAddButtonInPlayerList(((Boolean) event.getData()));
                                break;
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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

    @Override
    protected void onDetailPlayerClicked(PlayerResponse player) {
        int pick;
        if (player.getSelected()) {
            pick = PICK_PICKED;
        } else {
            pick = playerAdapter.getVisibleAddButton() == View.VISIBLE ? PICK_PICK : PICK_NONE;
        }

        PlayerDetailForLineupFragment.start(
                this,
                player,
                -1,
                getString(R.string.lineup),
                league.getGameplayOption(),
                pick,
                playerPosition == PlayerResponse.POSITION_NONE ? player.getMainPosition() : playerPosition,
                NONE_ORDER);
    }

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_ADD_PLAYER, args -> {
            Log.i(SocketEventKey.EVENT_ADD_PLAYER, "registerSocket: ");
            mActivity.runOnUiThread(this::refresh);
        });
    }

    public void visibleAddButtonInPlayerList(boolean visible) {
        if (visible) {
            if (playerAdapter.getVisibleAddButton() != View.VISIBLE) {
                playerAdapter.setVisibleAddButton(View.VISIBLE);
                playerAdapter.notifyDataSetChanged();
            }
        } else {
            if (playerAdapter.getVisibleAddButton() == View.VISIBLE) {
                playerAdapter.setVisibleAddButton(View.GONE);
                playerAdapter.notifyDataSetChanged();
            }
        }
    }
}
