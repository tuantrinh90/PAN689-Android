package com.football.fantasy.fragments.leagues.your_team.player_list;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.football.adapters.PlayerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.StatisticView;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PickEvent;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_pool.filter.FilterFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.utilities.AppUtilities;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public abstract class PlayerListFragment<V extends IPlayerListView, P extends IPlayerListPresenter<V>> extends BaseMvpFragment<V, P> implements IPlayerListView {
    private static final String TAG = "PlayerListFragment";

    static final String KEY_LEAGUE = "LEAGUE";

    public static PlayerListFragment newInstance(PlayerListFragment fragment, LeagueResponse league) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
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

    @BindView(R.id.tvTimeLabel)
    ExtTextView tvTimeLabel;
    @BindView(R.id.tvTime)
    ExtTextView tvTime;

    @BindView(R.id.svSearch)
    SearchView svSearchView;
    @BindView(R.id.ivSortValue)
    ImageView ivSortValue;
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    protected LeagueResponse league;
    private int page = 1;
    private String query = "";
    protected int playerPosition = -1;
    private View lastPlayerViewSelected = null;

    private boolean sortDesc = true;
    private String filterPositions = null;
    private String filterClubs = "";

    protected PlayerAdapter playerAdapter;

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

        // load data
        refresh();

        try {
            registerBus();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
        }
    }

    protected void initView() {
        displayTime();

        try {
            // disable statisticView
            svGoalkeeper.setSelected(false);
            svDefender.setSelected(false);
            svMidfielder.setSelected(false);
            svAttacker.setSelected(false);
            svNone.setSelected(true);
            lastPlayerViewSelected = svNone;

            // search view
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_blue_24_px);

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_players);

            // click button filter
            svSearchView.setFilerConsumer(v -> onClickFilter());

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query.trim()));

            // playerResponses

            playerAdapter = new PlayerAdapter(
                    getContext(),
                    player -> { // item click
                        onDetailPlayerClicked(player);
                    },
                    (player, position) -> { // add click
                        onAddPlayerClicked(player, position);
                    });

            rvPlayer.adapter(playerAdapter)
                    .loadMoreListener(() -> {
                        page++;
                        getPlayers(league.getSeasonId(), league.getId(), sortDesc, page, query, filterPositions, filterClubs);
                    })
                    .refreshListener(this::refresh)
                    .build();
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    protected void refresh() {
        page = 1;
        if (rvPlayer != null) {
            rvPlayer.clear();
            rvPlayer.startLoading();
        }
        getPlayers(league.getSeasonId(), league.getId(), sortDesc, page, query, filterPositions, filterClubs);
    }

    void displayTime() {
        if (AppUtilities.isSetupTime(league)) {
            tvTimeLabel.setText(R.string.team_setup_time);
            tvTime.setText(league.getTeamSetupFormatted());
        } else {
            tvTimeLabel.setText(R.string.start_time);
            tvTime.setText(league.getStartTimeFormatted());
        }
    }

    protected void registerBus() {
        // action add click onEvent PlayerList
        onEvent(PlayerQueryEvent.class, event -> {
            if (event.getFrom().equals(TAG))
                if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                    filterClubs = event.getClub();
                    filterPositions = event.getPosition();

                    refresh();
                }
        });

        // from: PlayerPopup & Lineup
        onEvent(PickEvent.class, event -> {
            int playerIndex = playerAdapter.findPlayerById(event.getPlayerId());
            if (playerIndex >= 0) {
                PlayerResponse player = playerAdapter.getItem(playerIndex);
                player.setSelected(event.getAction() == PickEvent.ACTION_PICK);
                playerAdapter.update(playerIndex, player);
            }
        });
    }

    void onClickFilter() {
        AloneFragmentActivity.with(this)
                .parameters(FilterFragment.newBundle(TAG, filterPositions, filterClubs, true))
                .start(FilterFragment.class);
    }

    void onPerformSearch(String q) {
        query = q;
        refresh();
    }

    @Override
    public void displayPlayers(List<PlayerResponse> data) {
        rvPlayer.addItems(data);
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
        if (!isLoading) {
            rvPlayer.stopLoading();
        }
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
                    filterPositions = null;
                    break;
                case R.id.svGoalkeeper:
                    playerPosition = PlayerResponse.POSITION_GOALKEEPER;
                    filterPositions = String.valueOf(StatisticView.Position.GOALKEEPER);
                    break;
                case R.id.svDefender:
                    playerPosition = PlayerResponse.POSITION_DEFENDER;
                    filterPositions = String.valueOf(StatisticView.Position.DEFENDER);
                    break;
                case R.id.svMidfielder:
                    playerPosition = PlayerResponse.POSITION_MIDFIELDER;
                    filterPositions = String.valueOf(StatisticView.Position.MIDFIELDER);
                    break;
                case R.id.svAttacker:
                    playerPosition = PlayerResponse.POSITION_ATTACKER;
                    filterPositions = String.valueOf(StatisticView.Position.ATTACKER);
                    break;
            }
            refresh();
        }
    }

    @OnClick(R.id.sortValue)
    public void onSortClick() {
        toggleSort();
        ivSortValue.setImageResource(sortDesc ? R.drawable.ic_sort_desc : R.drawable.ic_sort_asc);
    }

    private void toggleSort() {
        sortDesc = !sortDesc;
        refresh();
    }

    protected abstract void getPlayers(int seasonId, int leagueId, boolean valueSortDesc, int page, String query,
                                       String filterPositions, String filterClubs);

    protected abstract void onAddPlayerClicked(PlayerResponse player, Integer position);

    protected abstract void onDetailPlayerClicked(PlayerResponse player);
}
