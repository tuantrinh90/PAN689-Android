package com.football.fantasy.fragments.leagues.your_team.player_list;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.football.adapters.PlayerAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerListFragment extends BaseMainMvpFragment<IPlayerListView, IPlayerListPresenter<IPlayerListView>> implements IPlayerListView {
    private static final String TAG = "PlayerListFragment";

    static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    public static PlayerListFragment newInstance(Integer leagueId) {
        PlayerListFragment fragment = new PlayerListFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @BindView(R.id.tvSetupTime)
    ExtTextView tvSetupTime;
    @BindView(R.id.svSearch)
    SearchView svSearchView;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<PlayerResponse> playerResponses;
    PlayerAdapter playerAdapter;

    private int leagueId = 0;
    private String orderBy = "desc";
    private int page = 1;
    private String query = "";
    private String mainPosition = "";
    private int playerPosition = -1;
    private int lastPlayerId = -1;

    @Override
    public int getResourceId() {
        return R.layout.player_list_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            leagueId = bundle.getInt(KEY_LEAGUE_ID);
        }
    }

    void initView() {
        try {
            // search view
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_black_24_px);

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_players);

            // click button filter
            svSearchView.setFilerConsumer(v -> onClickFilter());

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query));

            // playerResponses
            playerAdapter = new PlayerAdapter(
                    mActivity,
                    playerResponses,
                    player -> { // item click

                    },
                    player -> { // add click
                        // bắn sang màn hình LineUp
                        bus.send(new PlayerEvent.Builder()
                                .action(PlayerEvent.ACTION_ADD_CLICK)
                                .position(playerPosition)
                                .data(player)
                                .build());
                    });
            rvRecyclerView.init(mActivity, playerAdapter)
                    .setOnExtLoadMoreListener(() -> {
                        page++;
                        presenter.getPlayers(leagueId, orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
                    })
                    .setOnExtRefreshListener(() -> Optional.from(rvRecyclerView).doIfPresent(rv -> {
                        page = 1;
                        rv.clearItems();
                        presenter.getPlayers(leagueId, orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
                    }));

            // load data
            presenter.getPlayers(leagueId, orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public IPlayerListPresenter<IPlayerListView> createPresenter() {
        return new PlayerListPresenter(getAppComponent());
    }

    void onClickFilter() {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            orderBy = orderBy.equalsIgnoreCase("desc") ? "asc" : "desc";
            svSearchView.getFilter().animate().rotation(orderBy.equalsIgnoreCase("desc") ? 0 : 180)
                    .setDuration(500).setInterpolator(new LinearInterpolator()).start();


        });
    }

    void onPerformSearch(String q) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            query = q;
            rv.clearItems();
            page = 1;
            presenter.getPlayers(leagueId, orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
        });
    }

    @Override
    public void notifyDataSetChangedPlayers(List<PlayerResponse> data) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(data));
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            if (isLoading) {
                rv.startLoading(true);
            } else {
                rv.stopLoading(true);
            }
        });
    }

    @OnClick({R.id.svNone, R.id.svGoalkeeper, R.id.svDefender, R.id.svMidfielder, R.id.svAttacker})
    public void onClicked(View view) {
        if (lastPlayerId != view.getId()) {
            // check/uncheck view
            if (lastPlayerId != -1) {
                view.getRootView().findViewById(lastPlayerId).setSelected(false);
            }
            view.setSelected(true);

            lastPlayerId = view.getId();

            switch (view.getId()) {
                case R.id.svNone:
                    playerPosition = PlayerResponse.POSITION_NONE;
                    break;
                case R.id.svGoalkeeper:
                    playerPosition = PlayerResponse.POSITION_GOALKEEPER;
                    break;
                case R.id.svDefender:
                    playerPosition = PlayerResponse.POSITION_DEFENDER;
                    break;
                case R.id.svMidfielder:
                    playerPosition = PlayerResponse.POSITION_MIDFIELDER;
                    break;
                case R.id.svAttacker:
                    playerPosition = PlayerResponse.POSITION_ATTACKER;
                    break;
            }
        }
    }
}
