package com.football.fantasy.fragments.leagues.player_pool;

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
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayerPoolFragment extends BaseMainMvpFragment<IPlayerPoolView, IPlayerPoolPresenter<IPlayerPoolView>> implements IPlayerPoolView {
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
        playerPoolItemAdapter = new PlayerPoolItemAdapter(mActivity, playerResponses);
        lvData.init(mActivity, playerPoolItemAdapter);

        getPlayers();
    }

    private void getPlayers() {
        presenter.getPlayers();

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
}
