package com.football.fantasy.fragments.leagues.player_pool.filter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.FilterAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_pool.display.IPlayerPoolDisplayPresenter;
import com.football.fantasy.fragments.leagues.player_pool.display.IPlayerPoolDisplayView;
import com.football.fantasy.fragments.leagues.player_pool.display.PlayerPoolDisplayPresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class PlayerPoolFilterFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {
    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.rvFilterByPosition)
    RecyclerView rvFilterByPosition;
    @BindView(R.id.rvFilterByClub)
    RecyclerView rvFilterByClub;
    @BindView(R.id.tvApplyFilter)
    ExtTextView tvApplyFilter;

    List<ExtKeyValuePair> keyValuePairPositions;
    List<ExtKeyValuePair> keyValuePairClubs;

    FilterAdapter filterPositionAdapter;
    FilterAdapter filterClubAdapter;

    @Override
    public int getResourceId() {
        return R.layout.player_pool_filter_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        // position
        keyValuePairPositions = new ArrayList<>();
        keyValuePairPositions.add(new ExtKeyValuePair(getString(R.string.all), getString(R.string.all)));
        keyValuePairPositions.add(new ExtKeyValuePair(getString(R.string.midfielder), getString(R.string.midfielder)));
        keyValuePairPositions.add(new ExtKeyValuePair(getString(R.string.goalkeeper), getString(R.string.goalkeeper)));
        keyValuePairPositions.add(new ExtKeyValuePair(getString(R.string.attacker), getString(R.string.attacker)));
        keyValuePairPositions.add(new ExtKeyValuePair(getString(R.string.defender), getString(R.string.defender)));

        filterPositionAdapter = new FilterAdapter(mActivity, keyValuePairPositions, extKeyValuePair -> {
            extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
            filterPositionAdapter.notifyDataSetChanged(keyValuePairPositions);
        });

        rvFilterByPosition.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvFilterByPosition.setAdapter(filterPositionAdapter);

        // clubs
        keyValuePairClubs = new ArrayList<>();
        keyValuePairClubs.add(new ExtKeyValuePair(getString(R.string.all), getString(R.string.all)));
        keyValuePairClubs.add(new ExtKeyValuePair(getString(R.string.midfielder), getString(R.string.midfielder)));
        keyValuePairClubs.add(new ExtKeyValuePair(getString(R.string.goalkeeper), getString(R.string.goalkeeper)));
        keyValuePairClubs.add(new ExtKeyValuePair(getString(R.string.attacker), getString(R.string.attacker)));
        keyValuePairClubs.add(new ExtKeyValuePair(getString(R.string.defender), getString(R.string.defender)));

        filterClubAdapter = new FilterAdapter(mActivity, keyValuePairClubs, extKeyValuePair -> {
            extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
            filterClubAdapter.notifyDataSetChanged(keyValuePairClubs);
        });

        rvFilterByClub.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvFilterByClub.setAdapter(filterClubAdapter);
    }

    @Override
    public IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> createPresenter() {
        return new PlayerPoolDisplayPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.player_list;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }
}
