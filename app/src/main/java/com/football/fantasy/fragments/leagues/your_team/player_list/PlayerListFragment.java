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
import com.football.customizes.lineup.StatisticView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;

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

    @BindView(R.id.svNone)
    StatisticView svNone;
    @BindView(R.id.svGoalkeeper)
    StatisticView svGoalkeeper;
    @BindView(R.id.svDefender)
    StatisticView svDefender;
    @BindView(R.id.svMidfielder)
    StatisticView svMidfielder;
    @BindView(R.id.svAttacker)
    StatisticView svAttacker;
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
    private View lastPlayerViewSelected = null;

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
            // disable statisticView
            svGoalkeeper.setSelected(false);
            svDefender.setSelected(false);
            svMidfielder.setSelected(false);
            svAttacker.setSelected(false);
            svNone.setSelected(true);
            lastPlayerViewSelected = svNone;

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
                        getPlayers(false);
                    })
                    .setOnExtRefreshListener(() -> Optional.from(rvRecyclerView).doIfPresent(rv -> {
                        page = 1;
                        rv.clearItems();
                        getPlayers(false);
                    }));

            // load data
            getPlayers(false);
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
            getPlayers(false);
        });
    }

    @Override
    public void notifyDataSetChangedPlayers(List<PlayerResponse> data, boolean newPlayers) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            if (newPlayers) {
                rv.clearItems();
            }
            rv.addNewItems(data);
        });
    }

    @Override
    public void displayStatistic(StatisticResponse statistic) {
        svGoalkeeper.setCount(statistic.getGoalkeeper());
        svDefender.setCount(statistic.getDefender());
        svMidfielder.setCount(statistic.getMidfielder());
        svAttacker.setCount(statistic.getAttacker());
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
        if (lastPlayerViewSelected != view) {
            // check/uncheck view
            if (lastPlayerViewSelected != null) {
                lastPlayerViewSelected.setSelected(false);
            }
            view.setSelected(true);

            lastPlayerViewSelected = view;

            switch (view.getId()) {
                case R.id.svNone:
                    playerPosition = PlayerResponse.POSITION_NONE;
                    mainPosition = StatisticView.PositionValue.NONE;
                    break;
                case R.id.svGoalkeeper:
                    playerPosition = PlayerResponse.POSITION_GOALKEEPER;
                    mainPosition = StatisticView.PositionValue.GOALKEEPER;
                    break;
                case R.id.svDefender:
                    playerPosition = PlayerResponse.POSITION_DEFENDER;
                    mainPosition = StatisticView.PositionValue.DEFENDER;
                    break;
                case R.id.svMidfielder:
                    playerPosition = PlayerResponse.POSITION_MIDFIELDER;
                    mainPosition = StatisticView.PositionValue.MIDFIELDER;
                    break;
                case R.id.svAttacker:
                    playerPosition = PlayerResponse.POSITION_ATTACKER;
                    mainPosition = StatisticView.PositionValue.ATTACKER;
                    break;
            }
            page = 1;
            getPlayers(true);
        }
    }

    private void getPlayers(boolean newPlayers) {
        presenter.getPlayers(leagueId, orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition, newPlayers);
    }
}
