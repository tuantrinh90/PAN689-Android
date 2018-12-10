package com.football.fantasy.fragments.leagues.player_pool.display;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.adapters.FilterAdapter;
import com.football.common.fragments.BaseMvpFragment;
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

public class DisplayConfigFragment extends BaseMvpFragment<IDisplayConfigView, IDisplayConfigPresenter<IDisplayConfigView>> implements IDisplayConfigView {

    private static final String KEY_TRANSFER_MODE = "TRANSFER_MODE";
    private static final String KEY_FROM = "FROM";
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
    private boolean transferMode;
    private String filterDisplays;
    private String from;

    public static Bundle newBundle(boolean transferMode, String from, String filterDisplays) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(KEY_TRANSFER_MODE, transferMode);
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
        transferMode = getArguments().getBoolean(KEY_TRANSFER_MODE);
        from = getArguments().getString(KEY_FROM);
        filterDisplays = getArguments().getString(KEY_DISPLAY);
    }

    void initView() {
        // display
        keyValuePairs = new ArrayList<>();
        if (transferMode) {
            keyValuePairs.add(new ExtKeyValuePair(VALUE, getString(R.string.value), filterDisplays.contains(VALUE)));
        }
        keyValuePairs.add(new ExtKeyValuePair(POINT, getString(R.string.point), filterDisplays.contains(POINT)));
        keyValuePairs.add(new ExtKeyValuePair(GOALS, getString(R.string.goals), filterDisplays.contains(GOALS)));
        keyValuePairs.add(new ExtKeyValuePair(ASSISTS, getString(R.string.assists), filterDisplays.contains(ASSISTS)));
        keyValuePairs.add(new ExtKeyValuePair(CLEAN_SHEET, getString(R.string.clean_sheet), filterDisplays.contains(CLEAN_SHEET)));
        keyValuePairs.add(new ExtKeyValuePair(DUELS_THEY_WIN, getString(R.string.duels_they_win), filterDisplays.contains(DUELS_THEY_WIN)));
        keyValuePairs.add(new ExtKeyValuePair(PASSES, getString(R.string.passes), filterDisplays.contains(PASSES)));
        keyValuePairs.add(new ExtKeyValuePair(SHOTS, getString(R.string.shots), filterDisplays.contains(SHOTS)));
        keyValuePairs.add(new ExtKeyValuePair(SAVES, getString(R.string.saves), filterDisplays.contains(SAVES)));
        keyValuePairs.add(new ExtKeyValuePair(YELLOW_CARDS, getString(R.string.yellow_cards), filterDisplays.contains(YELLOW_CARDS)));
        keyValuePairs.add(new ExtKeyValuePair(DRIBBLES, getString(R.string.dribbles), filterDisplays.contains(DRIBBLES)));
        keyValuePairs.add(new ExtKeyValuePair(TURNOVERS, getString(R.string.turnovers), filterDisplays.contains(TURNOVERS)));
        keyValuePairs.add(new ExtKeyValuePair(BALLS_RECOVERED, getString(R.string.balls_recovered), filterDisplays.contains(BALLS_RECOVERED)));
        keyValuePairs.add(new ExtKeyValuePair(FOULS_COMMITTED, getString(R.string.fouls_committed), filterDisplays.contains(FOULS_COMMITTED)));

        for (ExtKeyValuePair pair : keyValuePairs) {
            if (pair.isSelected()) checkCount++;
        }

        filterAdapter = new FilterAdapter(
                mActivity,
                keyValuePairs,
                extKeyValuePair -> {
                    if (!extKeyValuePair.isSelected() && checkCount > 2) {
                        DialogUtils.messageBox(mActivity,
                                0,
                                getString(R.string.app_name),
                                getString(R.string.message_maximun_3_columns),
                                getString(R.string.ok), null);

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

    @NonNull
    @Override
    public IDisplayConfigPresenter<IDisplayConfigView> createPresenter() {
        return new DisplayConfigPresenter(getAppComponent());
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

    public static ExtKeyValuePair getOptionDisplayDefault1(Context context) {
        return new ExtKeyValuePair(VALUE, context.getString(R.string.value), true);
    }

    public static ExtKeyValuePair getOptionDisplayDefault2(Context context) {
        return new ExtKeyValuePair(POINT, context.getString(R.string.point), true);
    }

    public static ExtKeyValuePair getOptionDisplayDefault3(Context context) {
        return new ExtKeyValuePair(GOALS, context.getString(R.string.goals), true);
    }

    public static ExtKeyValuePair getOptionDisplayDefault4(Context context) {
        return new ExtKeyValuePair(ASSISTS, context.getString(R.string.assists), true);
    }
}
