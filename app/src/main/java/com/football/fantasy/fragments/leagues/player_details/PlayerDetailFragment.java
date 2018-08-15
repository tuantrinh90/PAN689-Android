package com.football.fantasy.fragments.leagues.player_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.adapters.PlayerStatisticAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PlayerRoundPointResponse;
import com.football.models.responses.PlayerStatisticMetaResponse;
import com.football.utilities.AppUtilities;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;

public class PlayerDetailFragment extends BaseMvpFragment<IPlayerDetailView, IPlayerDetailPresenter<IPlayerDetailView>> implements IPlayerDetailView {

    private static final String KEY_PLAYER = "PLAYER";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_PICK_ENABLE = "PICK_PICKED";
    private static final String KEY_MAIN_POSITION = "MAIN_POSITION";
    private static final String KEY_ORDER = "ORDER";
    private static final String KEY_TEAM_ID = "TEAM_ID";

    private static final String KEY_TOTAL = "TOTAL";
    private static final String KEY_LAST = "LAST";
    private static final String KEY_SEASON = "SEASON";
    private static final String KEY_LAST_5_ROUNDS = "LAST_5_ROUNDS";
    private static final String KEY_LAST_3_ROUNDS = "LAST_3_ROUNDS";
    private static final String KEY_POINTS = "POINTS";

    public static final int PICK_NONE = -1;
    public static final int PICK_PICK = 0;
    public static final int PICK_PICKED = 1;

    private static final ExtKeyValuePair DEFAULT_KEY = new ExtKeyValuePair(KEY_TOTAL, "Total statistics");

    @BindView(R.id.viewPick)
    View viewPick;
    @BindView(R.id.ivPick)
    ImageView ivPick;
    @BindView(R.id.tvPick)
    ExtTextView tvPick;
    @BindView(R.id.tvName)
    ExtTextView tvName;
    @BindView(R.id.tvMainPosition)
    ExtTextView tvMainPosition;
    @BindView(R.id.tvMinorPosition)
    ExtTextView tvMinorPosition;
    @BindView(R.id.llPosition)
    FlexboxLayout llPosition;
    @BindView(R.id.ivAvatar)
    CircleImageViewApp ivAvatar;
    @BindView(R.id.tvState)
    ExtTextView tvState;
    @BindView(R.id.tvPoints)
    ExtTextView tvPoints;
    @BindView(R.id.tvValue)
    ExtTextView tvValue;
    @BindView(R.id.tvFilter)
    ExtTextView tvFilter;
    @BindView(R.id.tvGoals)
    ExtTextView tvGoals;
    @BindView(R.id.tvAssists)
    ExtTextView tvAssists;
    @BindView(R.id.tvCleanSheet)
    ExtTextView tvCleanSheet;
    @BindView(R.id.tvDuelsWin)
    ExtTextView tvDuelsWin;
    @BindView(R.id.tvPasses)
    ExtTextView tvPasses;
    @BindView(R.id.tvShots)
    ExtTextView tvShots;
    @BindView(R.id.tvSaves)
    ExtTextView tvSaves;
    @BindView(R.id.tvYellowCards)
    ExtTextView tvYellowCards;
    @BindView(R.id.tvDribbles)
    ExtTextView tvDribbles;
    @BindView(R.id.tvTurnovers)
    ExtTextView tvTurnovers;
    @BindView(R.id.tvBallsRecovered)
    ExtTextView tvBallsRecovered;
    @BindView(R.id.tvFoulsCommitted)
    ExtTextView tvFoulsCommitted;

    @BindView(R.id.llStatistic)
    View statisticGroup;
    @BindView(R.id.llStatisticList)
    View statisticListGroup;
    @BindView(R.id.rvStatistics)
    ExtPagingListView rvStatistics;

    private String title;
    private PlayerResponse player;
    private int pickEnable = PICK_NONE;
    private int mainPosition = PlayerResponse.POSITION_NONE;
    private int order = NONE_ORDER;
    private int teamId;

    List<ExtKeyValuePair> valuePairs = new ArrayList<>();
    private ExtKeyValuePair keyValuePairKey = DEFAULT_KEY;

    public static void start(Fragment fragment, PlayerResponse player, String title) {
        AloneFragmentActivity.with(fragment)
                .parameters(newBundle(player, title, PICK_NONE))
                .start(PlayerDetailFragment.class);
    }

    public static Bundle newBundle(String title, PlayerResponse player, int teamId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_PLAYER, player);
        bundle.putInt(KEY_TEAM_ID, teamId);
        return bundle;
    }

    public static Bundle newBundle(PlayerResponse player, String title, int pickEnable, int mainPosition, int order) {
        Bundle bundle = newBundle(player, title, pickEnable);
        bundle.putInt(KEY_MAIN_POSITION, mainPosition);
        bundle.putInt(KEY_ORDER, order);
        return bundle;
    }

    public static Bundle newBundle(PlayerResponse player, String title, int pick) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PLAYER, player);
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_PICK_ENABLE, pick);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_detail_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initData();
        initView();
        displayPlayer();
        initRecyclerView();

        getPlayerStatistic();
    }

    private void getPlayerStatistic() {
        String property;
        String value;

        switch (keyValuePairKey.getKey()) {
            case KEY_TOTAL:
                property = "total";
                value = "all";
                break;
            case KEY_LAST:
                property = "avg";
                value = "1";
                break;
            case KEY_SEASON:
                property = "avg";
                value = "all";
                break;
            case KEY_LAST_5_ROUNDS:
                property = "avg";
                value = "5";
                break;
            case KEY_LAST_3_ROUNDS:
                property = "avg";
                value = "3";
                break;
            case KEY_POINTS:
                property = "points_per_round";
                value = "all";
                break;
            default:
                property = "total";
                value = "all";
                break;
        }
        presenter.getPlayerStatistic(player.getId(), teamId, property, value);
    }

    private void initRecyclerView() {
        PlayerStatisticAdapter mAdapter = new PlayerStatisticAdapter(
                mActivity,
                new ArrayList<>());
        rvStatistics.init(mActivity, mAdapter)
                .setOnExtRefreshListener(() -> {
//                    page = 1;
                    rvStatistics.clearItems();
                    rvStatistics.startLoading();
                    updateValue();
                });
    }

    private void displayPlayer() {
        if (player != null) {
            tvName.setText(player.getName());
            AppUtilities.displayPlayerPosition(tvMainPosition, player.getMainPosition(), player.getMainPositionFullText());
            AppUtilities.displayPlayerPosition(tvMinorPosition, player.getMinorPosition(), player.getMinorPositionFullText());
            tvValue.setText(getString(R.string.money_prefix, player.getTransferValueDisplay()));
            ImageLoaderUtils.displayImage(player.getPhoto(), ivAvatar.getImageView());
            tvState.setVisibility(player.getInjured() ? View.VISIBLE : View.GONE);
            tvState.setText(player.getInjuredText(getContext()));
        }
    }

    private void getDataFromBundle() {
        player = (PlayerResponse) getArguments().getSerializable(KEY_PLAYER);
        title = getArguments().getString(KEY_TITLE);
        pickEnable = getArguments().getInt(KEY_PICK_ENABLE);
        mainPosition = getArguments().getInt(KEY_MAIN_POSITION);
        order = getArguments().getInt(KEY_ORDER);
        teamId = getArguments().getInt(KEY_TEAM_ID);
    }

    void initData() {
        valuePairs.add(DEFAULT_KEY);
        valuePairs.add(new ExtKeyValuePair(KEY_LAST, "Last round"));
        valuePairs.add(new ExtKeyValuePair(KEY_SEASON, "Avg of the season"));
        valuePairs.add(new ExtKeyValuePair(KEY_LAST_5_ROUNDS, "Avg of Last 5 rounds"));
        valuePairs.add(new ExtKeyValuePair(KEY_LAST_3_ROUNDS, "Avg of Last 3 rounds"));
        valuePairs.add(new ExtKeyValuePair(KEY_POINTS, "Points per round"));
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white_blue)));
        tvFilter.setText(keyValuePairKey.getValue());

        switch (pickEnable) {
            case PICK_NONE:
                viewPick.setVisibility(View.GONE);
                break;

            case PICK_PICK:
                viewPick.setVisibility(View.VISIBLE);
                tvPick.setText(getString(R.string.pick));
                tvPick.setTextColor(ContextCompat.getColor(mActivity, R.color.color_blue_end));
                ivPick.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_blue_end));
                viewPick.setBackgroundResource(R.drawable.bg_pick);
                break;

            case PICK_PICKED:
                viewPick.setVisibility(View.VISIBLE);
                tvPick.setText(getString(R.string.picked));
                tvPick.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white));
                ivPick.setColorFilter(ContextCompat.getColor(mActivity, R.color.color_white));
                viewPick.setBackgroundResource(R.drawable.bg_picked);
                break;
        }
    }

    @NonNull
    @Override
    public IPlayerDetailPresenter<IPlayerDetailView> createPresenter() {
        return new PlayerDetailPresenter(getAppComponent());
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @OnClick({R.id.selection})
    public void onClicked(View view) {
        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairs)
                .setValue(keyValuePairKey == null ? "" : keyValuePairKey.getKey())
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        keyValuePairKey = extKeyValuePair;
                        updateValue();
                    }
                }).show(getFragmentManager(), null);
    }

    @OnClick({R.id.viewPick})
    public void onMenuClicked(View view) {
        List<ExtKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair("Pick", "Pick"));

        ExtKeyValuePairDialogFragment.newInstance()
                .setExtKeyValuePairs(valuePairs)
                .setValue("")
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        showLoading(true);
                        // bắn sang MH Lineup
                        bus.send(new PlayerEvent.Builder()
                                .action(PlayerEvent.ACTION_ADD_CLICK)
                                .position(mainPosition)
                                .order(order)
                                .data(player)
                                .callback((success, message) -> {
                                    showLoading(false);
                                    if (success) {
                                        mActivity.finish();
                                    } else {
                                        showMessage(message);
                                    }
                                })
                                .build());
                    }
                }).show(getFragmentManager(), null);
    }

    private void updateValue() {
        tvFilter.setText(keyValuePairKey.getValue());
        getPlayerStatistic();
    }

    @Override
    public void displayPoints(Integer totalPoint) {
        tvPoints.setText(String.valueOf(totalPoint));
    }

    @Override
    public void displayStatistic(PlayerStatisticMetaResponse meta) {
        statisticGroup.setVisibility(View.VISIBLE);
        statisticListGroup.setVisibility(View.GONE);

        tvGoals.setText(String.valueOf(meta.getGoals()));
        tvAssists.setText(String.valueOf(meta.getAssists()));
        tvCleanSheet.setText(String.valueOf(meta.getCleanSheet()));
        tvDuelsWin.setText(String.valueOf(meta.getDuelsTheyWin()));
        tvPasses.setText(String.valueOf(meta.getPasses()));
        tvShots.setText(String.valueOf(meta.getShots()));
        tvSaves.setText(String.valueOf(meta.getSaves()));
        tvYellowCards.setText(String.valueOf(meta.getYellowCards()));
        tvDribbles.setText(String.valueOf(meta.getDribbles()));
        tvTurnovers.setText(String.valueOf(meta.getTurnovers()));
        tvBallsRecovered.setText(String.valueOf(meta.getBallsRecovered()));
        tvFoulsCommitted.setText(String.valueOf(meta.getFoulsCommitted()));
    }

    @Override
    public void displayStatistics(List<PlayerRoundPointResponse> metas) {
        statisticGroup.setVisibility(View.GONE);
        statisticListGroup.setVisibility(View.VISIBLE);

        Optional.from(rvStatistics).doIfPresent(rv -> rv.addNewItems(metas));
    }
}
