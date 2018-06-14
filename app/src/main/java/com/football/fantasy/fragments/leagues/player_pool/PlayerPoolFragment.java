package com.football.fantasy.fragments.leagues.player_pool;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.PlayerPoolItemAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayFragment;
import com.football.fantasy.fragments.leagues.player_pool.filter.PlayerPoolFilterFragment;
import com.football.models.responses.PlayerResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static android.app.Activity.RESULT_OK;

public class PlayerPoolFragment extends BaseMainMvpFragment<IPlayerPoolView, IPlayerPoolPresenter<IPlayerPoolView>> implements IPlayerPoolView {

    private static final int REQUEST_FILTER = 100;
    private static final int REQUEST_DISPLAY = 101;
    private static final int REQUEST_SORT = 102;

    @BindView(R.id.ivArrowLeft)
    ImageView ivArrowLeft;
    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.ivArrowRight)
    ImageView ivArrowRight;
    @BindView(R.id.svSearch)
    SearchView svSearch;
    @BindView(R.id.lvData)
    ExtPagingListView lvData;

    List<PlayerResponse> playerResponses;
    PlayerPoolItemAdapter playerPoolItemAdapter;
    private int page;
    private String filterClubs;
    private String filterPositions;

    public static Bundle newBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_pool_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initData();
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initData() {
        playerResponses = new ArrayList<>();
        playerPoolItemAdapter = new PlayerPoolItemAdapter(
                mActivity,
                playerResponses,
                player -> { // click event
                    AloneFragmentActivity.with(this)
                            .parameters(PlayerDetailFragment.newBundle(
                                    player,
                                    getString(R.string.player_pool)))
                            .start(PlayerDetailFragment.class);
                });
        lvData.init(mActivity, playerPoolItemAdapter)
                .setOnExtRefreshListener(() -> {
                    page = 1;
                    lvData.clearItems();
                    lvData.startLoading();
                    getPlayers();
                });

        lvData.startLoading();
        getPlayers();
    }

    private void getPlayers() {
        presenter.getPlayers(filterPositions, filterClubs);
    }

    @Override
    public IPlayerPoolPresenter<IPlayerPoolView> createPresenter() {
        return new PlayerPoolPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.league_details;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    @Override
    public void displayPlayers(List<PlayerResponse> players) {
        Optional.from(lvData).doIfPresent(rv -> {
            rv.addNewItems(players);
        });
    }

    @OnClick({R.id.filter, R.id.display})
    public void onSortClicked(View view) {
        switch (view.getId()) {
            case R.id.filter:
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_FILTER)
                        .parameters(PlayerPoolFilterFragment.newBundle())
                        .start(PlayerPoolFilterFragment.class);
                break;
            case R.id.display:
                AloneFragmentActivity.with(this)
                        .forResult(REQUEST_DISPLAY)
                        .parameters(PlayerPoolDisplayFragment.newBundle())
                        .start(PlayerPoolDisplayFragment.class);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_FILTER:
                if (resultCode == RESULT_OK) {
                    filterClubs = data.getStringExtra(PlayerPoolFilterFragment.KEY_POSITION);
                    filterPositions = data.getStringExtra(PlayerPoolFilterFragment.KEY_POSITION);
                    getPlayers();
                }
                break;
            case REQUEST_DISPLAY:

                break;
            case REQUEST_SORT:

                break;
        }
    }
}
