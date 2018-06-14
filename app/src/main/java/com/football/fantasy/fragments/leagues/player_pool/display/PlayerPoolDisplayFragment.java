package com.football.fantasy.fragments.leagues.player_pool.display;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.FilterAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class PlayerPoolDisplayFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {

    public static final String DISPLAY_DEFAULT = "transfer_value,point,goals";

    private static final String KEY_DISPLAY = "DISPLAY";

    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.tvApplyToTable)
    ExtTextView tvApplyToTable;

    List<ExtKeyValuePair> keyValuePairs;
    FilterAdapter filterAdapter;

    private int checkCount = 0;
    private String filterDisplays;

    public static Bundle newBundle(String filterDisplays) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_DISPLAY, filterDisplays);
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
        getDataFromBundle();
        initView();
    }

    private void getDataFromBundle() {
        filterDisplays = getArguments().getString(KEY_DISPLAY);
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
        // display
        keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new ExtKeyValuePair("transfer_value", "Value", filterDisplays.contains("transfer_value")));
        keyValuePairs.add(new ExtKeyValuePair("point", "Point", filterDisplays.contains("point")));
        keyValuePairs.add(new ExtKeyValuePair("goals", "Stat 1", filterDisplays.contains("goals")));
        keyValuePairs.add(new ExtKeyValuePair("assists", "Stat 2", filterDisplays.contains("assists")));
        keyValuePairs.add(new ExtKeyValuePair("clean_sheet", "Stat 3", filterDisplays.contains("clean_sheet")));
        keyValuePairs.add(new ExtKeyValuePair("duels_they_win", "Stat 4", filterDisplays.contains("duels_they_win")));
        keyValuePairs.add(new ExtKeyValuePair("passes", "Stat 5", filterDisplays.contains("passes")));
        keyValuePairs.add(new ExtKeyValuePair("shots", "Stat 6", filterDisplays.contains("shots")));
        keyValuePairs.add(new ExtKeyValuePair("saves", "Stat 7", filterDisplays.contains("saves")));
        keyValuePairs.add(new ExtKeyValuePair("yellow_cards", "Stat 8", filterDisplays.contains("yellow_cards")));
        keyValuePairs.add(new ExtKeyValuePair("dribbles", "Stat 9", filterDisplays.contains("dribbles")));
        keyValuePairs.add(new ExtKeyValuePair("turnovers", "Stat 10", filterDisplays.contains("turnovers")));
        keyValuePairs.add(new ExtKeyValuePair("balls_recovered", "Stat 11", filterDisplays.contains("balls_recovered")));
        keyValuePairs.add(new ExtKeyValuePair("fouls_committed", "Stat 12", filterDisplays.contains("fouls_committed")));

        for (ExtKeyValuePair pair : keyValuePairs) {
            if (pair.isSelected()) checkCount++;
        }

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
        StringBuilder stringBuilder = new StringBuilder();
        for (ExtKeyValuePair valuePair : keyValuePairs) {
            if (valuePair.isSelected()) {
                stringBuilder.append(valuePair.getKey()).append(",");
            }
        }
        String displays = TextUtils.isEmpty(stringBuilder) ? "" : stringBuilder.substring(0, stringBuilder.lastIndexOf(","));

        // bắn sang màn hình playerPoolFragment
        bus.send(new PlayerQueryEvent.Builder()
                .tag(PlayerQueryEvent.TAG_DISPLAY)
                .display(displays)
                .build());

        getActivity().finish();

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
