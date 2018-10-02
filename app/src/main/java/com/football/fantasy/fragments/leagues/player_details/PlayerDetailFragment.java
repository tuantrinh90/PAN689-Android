package com.football.fantasy.fragments.leagues.player_details;

import android.content.Context;
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

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class PlayerDetailFragment extends BaseMvpFragment<IPlayerDetailView, IPlayerDetailPresenter<IPlayerDetailView>> implements IPlayerDetailView {

    private static final String KEY_PLAYER_ID = "PLAYER_ID";
    private static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_PICK_ENABLE = "PICK_PICKED";
    private static final String KEY_GAMEPLAY_OPTION = "GAMEPLAY_OPTION";

    private static final String KEY_TOTAL = "TOTAL";
    private static final String KEY_LAST = "LAST";
    private static final String KEY_SEASON = "SEASON";
    private static final String KEY_LAST_5_ROUNDS = "LAST_5_ROUNDS";
    private static final String KEY_LAST_3_ROUNDS = "LAST_3_ROUNDS";
    private static final String KEY_POINTS = "POINTS";

    public static final int PICK_NONE = -2;
    public static final int PICK_NONE_INFO = -1;
    public static final int PICK_PICK = 0;
    public static final int PICK_PICKED = 1;

    private static final ExtKeyValuePair DEFAULT_KEY = new ExtKeyValuePair(KEY_TOTAL, "Total statistics");

    @BindView(R.id.ivInfo)
    View ivInfo;
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

    @BindView(R.id.budget)
    View budget;

    private String title;
    protected int playerId;
    protected int pickEnable = PICK_NONE;
    protected int teamId;
    private String gameplayOption;

    protected PlayerResponse player;

    List<ExtKeyValuePair> valuePairs = new ArrayList<>();
    private ExtKeyValuePair keyValuePairKey = DEFAULT_KEY;

    // only for view PlayerDetail
    public static void start(Context context, int playerId, int teamId, String title, int pick, String gameplayOption) {
        AloneFragmentActivity.with(context)
                .parameters(newBundle(playerId, teamId, title, pick, gameplayOption))
                .start(PlayerDetailFragment.class);
    }

    public static Bundle newBundle(int playerId, int teamId, String title, int pick, String gameplayOption) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_PLAYER_ID, playerId);
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_PICK_ENABLE, pick);
        bundle.putString(KEY_GAMEPLAY_OPTION, gameplayOption);
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
        initRecyclerView();

        getPlayerDetail();
        getPlayerStatistic();
    }

    private void getPlayerDetail() {
        presenter.getPlayerDetail(playerId);
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
        presenter.getPlayerStatistic(playerId, teamId, property, value);
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

    protected void getDataFromBundle() {
        playerId = getArguments().getInt(KEY_PLAYER_ID);
        title = getArguments().getString(KEY_TITLE);
        teamId = getArguments().getInt(KEY_TEAM_ID);
        pickEnable = getArguments().getInt(KEY_PICK_ENABLE, PICK_NONE);
        gameplayOption = getArguments().getString(KEY_GAMEPLAY_OPTION, GAMEPLAY_OPTION_TRANSFER);
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

            case PICK_NONE_INFO:
                viewPick.setVisibility(View.GONE);
                ivInfo.setVisibility(View.VISIBLE);
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

        budget.setVisibility(
                TextUtils.isEmpty(gameplayOption) || gameplayOption.equals(GAMEPLAY_OPTION_TRANSFER) ?
                        View.VISIBLE : View.GONE);
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

    @OnClick({R.id.ivInfo})
    public void onInfoClicked() {
        showMessage(getString(R.string.message_info_player_detail));
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
//        bus.send(new TransferEvent(player, player));
//        mActivity.finish();
    }

    private void updateValue() {
        tvFilter.setText(keyValuePairKey.getValue());
        getPlayerStatistic();
    }

    @Override
    public void displayPlayer(PlayerResponse player) {
        this.player = player;

        tvName.setText(player.getName());
        AppUtilities.displayPlayerPosition(tvMainPosition, player.getMainPosition(), player.getMainPositionFullText());
        AppUtilities.displayPlayerPosition(tvMinorPosition, player.getMinorPosition(), player.getMinorPositionFullText());
        tvValue.setText(getString(R.string.money_prefix, player.getTransferValueDisplay()));
        ImageLoaderUtils.displayImage(player.getPhoto(), ivAvatar.getImageView());
        tvState.setVisibility(player.getInjured() ? View.VISIBLE : View.GONE);
        tvState.setText(player.getInjuredText(getContext()));
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
