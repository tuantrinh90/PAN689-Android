package com.football.fantasy.fragments.leagues.your_team.players_popup;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtPagingListView;
import com.football.adapters.PlayerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPopupFragment extends BaseMainMvpFragment<IPlayerPopupView, IPlayerPopupPresenter<IPlayerPopupView>> implements IPlayerPopupView {

    private static final String KEY_POSITION = "POSITION";
    private static final String KEY_INDEX = "INDEX";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    @BindView(R.id.svSearch)
    SearchView svSearchView;
    @BindView(R.id.ivSortValue)
    ImageView ivSortValue;
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    PlayerAdapter playerAdapter;

    private int leagueId = 0;
    private int page = 1;
    private String query = "";
    private Integer mainPosition = null;
    private int index;

    private int valueDirection = Constant.SORT_NONE;
    private String filterClubs = "";

    public static PlayerPopupFragment newInstance() {
        return new PlayerPopupFragment();
    }

    public static Bundle newBundle(int position, Integer index, int leagueId) {
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        args.putInt(KEY_INDEX, index);
        args.putInt(KEY_LEAGUE_ID, leagueId);
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
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            index = bundle.getInt(KEY_INDEX);
            leagueId = bundle.getInt(KEY_LEAGUE_ID);
            mainPosition = bundle.getInt(KEY_POSITION);
        }
    }

    @NonNull
    @Override
    public IPlayerPopupPresenter<IPlayerPopupView> createPresenter() {
        return new PlayerPopupDataPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.line_up;
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
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_black_24_px);
            svSearchView.setFilerConsumer(aVoid -> {
                AloneFragmentActivity.with(this)
                        .forResult(100)
                        .parameters(PlayerPoolFilterFragment.newBundle(String.valueOf(mainPosition), filterClubs, true))
                        .start(PlayerPoolFilterFragment.class);
            });

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_players);

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query));

            // playerResponses
            playerAdapter = new PlayerAdapter(
                    new ArrayList<>(),
                    player -> { // item click
                        AloneFragmentActivity.with(this)
                                .parameters(PlayerDetailFragment.newBundle(
                                        player,
                                        getString(R.string.player_list)))
                                .start(PlayerDetailFragment.class);
                    },
                    player -> { // add click
                        // bắn sang màn hình LineUp
                        bus.send(new PlayerEvent.Builder()
                                .action(PlayerEvent.ACTION_ADD_CLICK)
                                .position(mainPosition)
                                .index(index)
                                .data(player)
                                .build());
                    });
            rvPlayer.adapter(playerAdapter)
                    .loadMoreListener(() -> {
                        page++;
                        getPlayers();
                    })
                    .refreshListener(() -> {
                        page = 1;
                        rvPlayer.clear();
                        rvPlayer.startLoading();
                        getPlayers();
                    })
                    .build();

            // load data
            rvPlayer.startLoading();
            getPlayers();
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    void onPerformSearch(String q) {
        query = q;
        rvPlayer.clear();
        rvPlayer.startLoading();
        page = 1;
        getPlayers();
    }

    private void getPlayers() {
        presenter.getPlayers(leagueId, valueDirection, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
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
        rvPlayer.clear();
        rvPlayer.startLoading();
        getPlayers();
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
