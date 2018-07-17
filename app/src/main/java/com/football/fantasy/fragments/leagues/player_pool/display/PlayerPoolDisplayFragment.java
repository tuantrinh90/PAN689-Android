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
import com.football.events.PlayerQueryEvent;
import com.football.fantasy.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.models.responses.PlayerResponse.Options.ASSISTS;
import static com.football.models.responses.PlayerResponse.Options.BALLS_RECOVERED;
import static com.football.models.responses.PlayerResponse.Options.CLEAN_SHEET;
import static com.football.models.responses.PlayerResponse.Options.DRIBBLES;
import static com.football.models.responses.PlayerResponse.Options.DUELS_THEY_WIN;
import static com.football.models.responses.PlayerResponse.Options.FOULS_COMMITTED;
import static com.football.models.responses.PlayerResponse.Options.GOALS;
import static com.football.models.responses.PlayerResponse.Options.PASSES;
import static com.football.models.responses.PlayerResponse.Options.POINT;
import static com.football.models.responses.PlayerResponse.Options.SAVES;
import static com.football.models.responses.PlayerResponse.Options.SHOTS;
import static com.football.models.responses.PlayerResponse.Options.TURNOVERS;
import static com.football.models.responses.PlayerResponse.Options.VALUE;
import static com.football.models.responses.PlayerResponse.Options.YELLOW_CARDS;

public class PlayerPoolDisplayFragment extends BaseMainMvpFragment<IPlayerPoolDisplayView, IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView>> implements IPlayerPoolDisplayView {

    private static final String KEY_FROM = "FROM";

    public static final ExtKeyValuePair OPTION_DISPLAY_DEFAULT_1 = new ExtKeyValuePair(VALUE, "Value", true); // selected = sort by desc
    public static final ExtKeyValuePair OPTION_DISPLAY_DEFAULT_2 = new ExtKeyValuePair(POINT, "Point", true);
    public static final ExtKeyValuePair OPTION_DISPLAY_DEFAULT_3 = new ExtKeyValuePair(GOALS, "Goals", true);

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
    private String from;

    public static Bundle newBundle(String from, String filterDisplays) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FROM, from);
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
        from = getArguments().getString(KEY_FROM);
        filterDisplays = getArguments().getString(KEY_DISPLAY);
    }

    void initView() {
        // display
        keyValuePairs = new ArrayList<>();
        keyValuePairs.add(new ExtKeyValuePair(VALUE, "Value", filterDisplays.contains(VALUE)));
        keyValuePairs.add(new ExtKeyValuePair(POINT, "Point", filterDisplays.contains(POINT)));
        keyValuePairs.add(new ExtKeyValuePair(GOALS, "Goals", filterDisplays.contains(GOALS)));
        keyValuePairs.add(new ExtKeyValuePair(ASSISTS, "Assists", filterDisplays.contains(ASSISTS)));
        keyValuePairs.add(new ExtKeyValuePair(CLEAN_SHEET, "Clean Sheet", filterDisplays.contains(CLEAN_SHEET)));
        keyValuePairs.add(new ExtKeyValuePair(DUELS_THEY_WIN, "Duels They Win", filterDisplays.contains(DUELS_THEY_WIN)));
        keyValuePairs.add(new ExtKeyValuePair(PASSES, "Passes", filterDisplays.contains(PASSES)));
        keyValuePairs.add(new ExtKeyValuePair(SHOTS, "Shots", filterDisplays.contains(SHOTS)));
        keyValuePairs.add(new ExtKeyValuePair(SAVES, "Saves", filterDisplays.contains(SAVES)));
        keyValuePairs.add(new ExtKeyValuePair(YELLOW_CARDS, "Yellow Cards", filterDisplays.contains(YELLOW_CARDS)));
        keyValuePairs.add(new ExtKeyValuePair(DRIBBLES, "Dribbles", filterDisplays.contains(DRIBBLES)));
        keyValuePairs.add(new ExtKeyValuePair(TURNOVERS, "Turnovers", filterDisplays.contains(TURNOVERS)));
        keyValuePairs.add(new ExtKeyValuePair(BALLS_RECOVERED, "Balls Recovered", filterDisplays.contains(BALLS_RECOVERED)));
        keyValuePairs.add(new ExtKeyValuePair(FOULS_COMMITTED, "Fouls Committed", filterDisplays.contains(FOULS_COMMITTED)));

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

    @OnClick(R.id.tvApplyToTable)
    void onClickApplyToTable() {
        List<ExtKeyValuePair> displays = new ArrayList<>();

        for (ExtKeyValuePair valuePair : keyValuePairs) {
            if (valuePair.isSelected()) {
                displays.add(valuePair);
            }
        }
        // bắn sang màn hình playerPoolFragment
        bus.send(new PlayerQueryEvent.Builder()
                .from(from)
                .tag(PlayerQueryEvent.TAG_DISPLAY)
                .displays(displays)
                .build());

        getActivity().finish();

    }
}
