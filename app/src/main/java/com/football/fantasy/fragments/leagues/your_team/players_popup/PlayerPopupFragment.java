package com.football.fantasy.fragments.leagues.your_team.players_popup;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.football.adapters.PlayerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.GeneralEvent;
import com.football.events.PickEvent;
import com.football.events.PlayerEvent;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.FilterFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPopupFragment extends BaseMvpFragment<IPlayerPopupView, IPlayerPopupPresenter<IPlayerPopupView>> implements IPlayerPopupView {

    private static final String TAG = "PlayerPopupFragment";

    private static final String KEY_SEASON_ID = "SEASON_ID";
    private static final String KEY_POSITION = "POSITION";
    private static final String KEY_ORDER = "ORDER";
    private static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.svSearch)
    SearchView svSearchView;
    @BindView(R.id.ivSortValue)
    ImageView ivSortValue;
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private LeagueResponse league;
    private int page = 1;
    private String query = "";
    private Integer mainPosition = null;
    private int order;

    private int valueDirection = Constant.SORT_NONE;
    private String filterClubs = "";
    private PlayerAdapter adapter;


    public static Bundle newBundle(int position, Integer order, LeagueResponse league) {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_ORDER, order);
        args.putSerializable(KEY_LEAGUE, league);
        return args;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_popup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        getPlayers();
        registerBus();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            order = bundle.getInt(KEY_ORDER);
            league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
            mainPosition = bundle.getInt(KEY_POSITION);
        }
    }

    private void registerBus() {
        // action add click onEvent PlayerList
        onEvent(PlayerQueryEvent.class, event -> {
            if (event.getFrom().equals(TAG)) {
                if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                    filterClubs = event.getClub();

                    refresh();
                }
            }
        });

        // pick Player
        // action add click onEvent PlayerList
        onEvent(PickEvent.class, event -> {
            if (event.getAction() == PickEvent.DONE) {
                mActivity.finish();
            }
        });

        onEvent(GeneralEvent.class, event -> {
            switch (event.getSource()) {
                case LINEUP_DRAFT:
                    visibleAddButtonInPlayerList(((Boolean) event.getData()));
                    break;
            }
        });

//        onEvent(PlayerEvent.class, event -> {
//            switch (event.getAction()) {
//                case PlayerEvent.ACTION_ADD_CLICK:
//                    mActivity.finish();
//                    break;
//            }
//        });
    }

    // bắn sang màn hình LineUp
    private void sendToLineup(PlayerResponse player) {
        showLoading(true);
        bus.send(new PlayerEvent.Builder()
                .action(PlayerEvent.ACTION_ADD_CLICK)
                .position(mainPosition)
                .order(order)
                .data(player)
                .callback((success, message) -> {
                    showLoading(false);
                    if (success) {
                        // bắn về cho PlayerList để cập nhật trạng thái đã pick
                        mCompositeDisposable.dispose();
                        bus.send(new PickEvent(PickEvent.ACTION_PICK, player.getId()));

                        mActivity.finish();
                    } else {
                        showMessage(message);
                    }
                })
                .build());
    }

    @NonNull
    @Override
    public IPlayerPopupPresenter<IPlayerPopupView> createPresenter() {
        return new PlayerPopupDataPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.lineup;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    void initView() {
        try {
            // search view
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_blue_24_px);
            svSearchView.setFilerConsumer(aVoid -> {
                AloneFragmentActivity.with(this)
                        .parameters(FilterFragment.newBundle(TAG, String.valueOf(mainPosition), filterClubs, true))
                        .start(FilterFragment.class);
            });

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_players);

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query.trim()));

            // playerResponses
            adapter = new PlayerAdapter(
                    getContext(),
                    player -> { // item click
                        PlayerDetailForLineupFragment.start(this,
                                player,
                                -1,
                                getString(R.string.player_list),
                                league.getGameplayOption(),
                                player.getSelected() ? PlayerDetailFragment.PICK_PICKED : PlayerDetailFragment.PICK_PICK,
                                mainPosition,
                                order);
                    },
                    (player, position) -> { // add click
                        sendToLineup(player);
                    });
            if (league.equalsGameplay(LeagueResponse.GAMEPLAY_OPTION_DRAFT)) {
                adapter.setVisibleValue(View.GONE);
            }
            adapter.setVisibleAddButton(View.VISIBLE);
            rvPlayer.adapter(adapter)
                    .loadMoreListener(() -> {
                        page++;
                        getPlayers();
                    })
                    .refreshListener(this::refresh)
                    .build();

            // load data
            rvPlayer.startLoading();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    void onPerformSearch(String q) {
        query = q;
        refresh();
    }

    private void getPlayers() {
        presenter.getPlayers(league.getSeasonId(), league.getId(), valueDirection, page, query, mainPosition, filterClubs);
    }

    @OnClick(R.id.sortValue)
    public void onSortClick() {
        toggleSort();
        ivSortValue.setImageResource(getArrowResource(valueDirection));
    }

    private void toggleSort() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        switch (valueDirection) {
            case Constant.SORT_NONE:
                valueDirection = Constant.SORT_DESC;
                break;
            case Constant.SORT_DESC:
                valueDirection = Constant.SORT_ASC;
                break;
            case Constant.SORT_ASC:
                valueDirection = Constant.SORT_DESC;
                break;
        }

        refresh();
    }

    private int getArrowResource(int state) {
        switch (state) {
            case Constant.SORT_NONE:
                return R.drawable.ic_sort_none;

            case Constant.SORT_DESC:
                return R.drawable.ic_sort_desc;

            case Constant.SORT_ASC:
                return R.drawable.ic_sort_asc;
        }
        return R.drawable.ic_sort_none;
    }

    private void refresh() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers();
    }

    public void visibleAddButtonInPlayerList(boolean visible) {
        if (adapter == null) return;
        if (visible) {
            if (adapter.getVisibleAddButton() != View.VISIBLE) {
                adapter.setVisibleAddButton(View.VISIBLE);
                adapter.notifyDataSetChanged();
            }
        } else {
            if (adapter.getVisibleAddButton() == View.VISIBLE) {
                adapter.setVisibleAddButton(View.GONE);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvPlayer.stopLoading();
        }
    }

    @Override
    public void displayPlayers(List<PlayerResponse> data) {
        rvPlayer.addItems(data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
