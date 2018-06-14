package com.football.fantasy.fragments.leagues.player_pool.display;

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
import com.football.models.responses.RealClubResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPoolDisplayFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {
    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.tvApplyToTable)
    ExtTextView tvApplyToTable;

    List<ExtKeyValuePair> keyValuePairs;
    FilterAdapter filterAdapter;

    private int checkCount = 0;

    public static Bundle newBundle() {
        Bundle bundle = new Bundle();
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_pool_display_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    /**
     * transfer_value: Value
     * point: Point
     * goals: Stat 1
     * assists: Stat 2
     * clean_sheet: Stat 3
     * duels_they_win: Stat 4
     * passes: Stat 5
     * shots: Stat 6
     * saves: Stat 7
     * yellow_cards: Stat 8
     * dribbles: Stat 9
     * turnovers: Stat 10
     * balls_recovered: Stat 11
     * fouls_committed: Stat 12
     */
    void initView() {
        // position
        keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new ExtKeyValuePair("transfer_value", "Value"));
        keyValuePairs.add(new ExtKeyValuePair("point", "Point"));
        keyValuePairs.add(new ExtKeyValuePair("goals", "Stat 1"));
        keyValuePairs.add(new ExtKeyValuePair("assists", "Stat 2"));
        keyValuePairs.add(new ExtKeyValuePair("clean_sheet", "Stat 3"));
        keyValuePairs.add(new ExtKeyValuePair("duels_they_win", "Stat 4"));
        keyValuePairs.add(new ExtKeyValuePair("passes", "Stat 5"));
        keyValuePairs.add(new ExtKeyValuePair("shots", "Stat 6"));
        keyValuePairs.add(new ExtKeyValuePair("saves", "Stat 7"));
        keyValuePairs.add(new ExtKeyValuePair("yellow_cards", "Stat 8"));
        keyValuePairs.add(new ExtKeyValuePair("dribbles", "Stat 9"));
        keyValuePairs.add(new ExtKeyValuePair("turnovers", "Stat 10"));
        keyValuePairs.add(new ExtKeyValuePair("balls_recovered", "Stat 11"));
        keyValuePairs.add(new ExtKeyValuePair("fouls_committed", "Stat 12"));

        filterAdapter = new FilterAdapter(
                mActivity,
                keyValuePairs,
                extKeyValuePair -> {
                    if (!extKeyValuePair.isSelected() && checkCount > 2) {
                        return;
                    }
                    if (extKeyValuePair.isSelected()) checkCount--;
                    else checkCount++;
                    extKeyValuePair.setSelected(!extKeyValuePair.isSelected());
                    filterAdapter.notifyDataSetChanged();
                });

        rvRecyclerView.setLayoutManager(new GridLayoutManager(mActivity, 2));
        rvRecyclerView.setAdapter(filterAdapter);
    }

    @Override
    public IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> createPresenter() {
        return new PlayerPoolDisplayPresenter(getAppComponent());
    }

    @OnClick(R.id.tvApplyToTable)
    void onClickApplyToTable() {

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
