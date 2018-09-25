package com.football.fantasy.fragments.leagues.your_team.player_list;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.bon.util.DateTimeUtils;
import com.football.adapters.PlayerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.StatisticView;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PickEvent;
import com.football.events.PlayerEvent;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.StatisticResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICK;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICKED;

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

    private LeagueResponse league;
    private int page = 1;
    private String query = "";
    private int playerPosition = -1;
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
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        registerBus();
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
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_black_24_px);

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
                        PlayerDetailForLineupFragment.start(
                                this,
                                player,
                                -1,
                                getString(R.string.lineup),
                                league.getGameplayOption(),
                                player.getSelected() ? PICK_PICKED : PICK_PICK,
                                playerPosition == PlayerResponse.POSITION_NONE ? player.getMainPosition() : playerPosition,
                                NONE_ORDER);
                    },
                    (player, position) -> { // add click
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
                    });

            // đang ở setupTime && chưa completed
            boolean visibleAddButton = AppUtilities.isSetupTime(league.getTeamSetup());
//                    && (league.getTeam() != null && league.getTeam().getCompleted() != null && !league.getTeam().getCompleted());
            playerAdapter.setVisibleAddButton(visibleAddButton ? View.VISIBLE : View.GONE);
            rvPlayer.adapter(playerAdapter)
                    .loadMoreListener(() -> {
                        page++;
                        getPlayers(league.getId(), sortDesc, page, query, filterPositions, filterClubs);
                    })
                    .refreshListener(this::refresh)
                    .build();

            // load data
            refresh();
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    protected void refresh() {
        page = 1;
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers(league.getId(), sortDesc, page, query, filterPositions, filterClubs);
    }

    void displayTime() {
        if (AppUtilities.isSetupTime(league.getTeamSetup())) {
            tvTimeLabel.setText(R.string.team_setup_time);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));
        } else {
            tvTimeLabel.setText(R.string.start_time);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));
        }
    }

    private void registerBus() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(PlayerQueryEvent.class)
                    .subscribeWith(new DisposableObserver<PlayerQueryEvent>() {
                        @Override
                        public void onNext(PlayerQueryEvent event) {
                            if (event.getFrom().equals(TAG))
                                if (event.getTag() == PlayerQueryEvent.TAG_FILTER) {
                                    filterClubs = event.getClub();
                                    filterPositions = event.getPosition();

                                    refresh();
                                }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));

            // from: PlayerPopup & Lineup
            mCompositeDisposable.add(bus.ofType(PickEvent.class)
                    .subscribeWith(new DisposableObserver<PickEvent>() {
                        @Override
                        public void onNext(PickEvent event) {
                            int playerIndex = playerAdapter.findPlayerById(event.getPlayerId());
                            if (playerIndex >= 0) {
                                PlayerResponse player = playerAdapter.getItem(playerIndex);
                                player.setSelected(event.getAction() == PickEvent.ACTION_PICK);
                                playerAdapter.update(playerIndex, player);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void onClickFilter() {
        AloneFragmentActivity.with(this)
                .parameters(PlayerPoolFilterFragment.newBundle(TAG, filterPositions, filterClubs, true))
                .start(PlayerPoolFilterFragment.class);
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

    protected abstract void getPlayers(int leagueId, boolean valueSortDesc, int page, String query,
                                       String filterPositions, String filterClubs);
}
