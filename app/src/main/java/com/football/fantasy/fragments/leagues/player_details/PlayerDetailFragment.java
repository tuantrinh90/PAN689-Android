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
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.lineup.LineupView;
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

public class PlayerDetailFragment extends BaseMainMvpFragment<IPlayerDetailView, IPlayerDetailPresenter<IPlayerDetailView>> implements IPlayerDetailView {

    private static final String KEY_PLAYER = "PLAYER";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_PICK_ENABLE = "PICK_ENABLE";
    private static final String KEY_MAIN_POSITION = "MAIN_POSITION";
    private static final String KEY_ORDER = "ORDER";


    @BindView(R.id.ivMenu)
    ImageView ivMenu;
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

    private PlayerResponse player;
    private String title;
    private boolean pickEnable;
    private int mainPosition = PlayerResponse.POSITION_NONE;
    private int order = LineupView.NONE_ORDER;

    private ExtKeyValuePair keyValuePairKey = new ExtKeyValuePair("[{\"property\":\"total\", \"operator\":\"eq\",\"value\":\"all\"}]", "Total statistics");

    public static void start(Fragment fragment, PlayerResponse player, String title, boolean pickEnable) {
        AloneFragmentActivity.with(fragment)
                .parameters(newBundle(player, title, pickEnable))
                .start(PlayerDetailFragment.class);
    }

    public static Bundle newBundle(PlayerResponse player, String title, boolean pickEnable) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PLAYER, player);
        bundle.putString(KEY_TITLE, title);
        bundle.putBoolean(KEY_PICK_ENABLE, pickEnable);
        return bundle;
    }

    public static Bundle newBundle(PlayerResponse player, String title, boolean pickEnable, int mainPosition, int order) {
        Bundle bundle = newBundle(player, title, pickEnable);
        bundle.putInt(KEY_MAIN_POSITION, mainPosition);
        bundle.putInt(KEY_ORDER, order);
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
        initView();
        displayPlayer();
        initRecyclerView();

        presenter.getPlayerStatistic(player.getId(), null);
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
        pickEnable = getArguments().getBoolean(KEY_PICK_ENABLE);
        mainPosition = getArguments().getInt(KEY_MAIN_POSITION);
        order = getArguments().getInt(KEY_ORDER);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.color_white)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white_blue)));

        ivMenu.setVisibility(pickEnable ? View.VISIBLE : View.GONE);
        tvFilter.setText(keyValuePairKey.getValue());
    }

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
        List<ExtKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair("[{\"property\":\"total\", \"operator\":\"eq\",\"value\":\"all\"}]", "Total statistics"));
        valuePairs.add(new ExtKeyValuePair("[{\"property\":\"avg\", \"operator\":\"eq\",\"value\":\"1\"}]", "Last round"));
        valuePairs.add(new ExtKeyValuePair("[{\"property\":\"avg\", \"operator\":\"eq\",\"value\":\"all\"}]", "Avg of the season"));
        valuePairs.add(new ExtKeyValuePair("[{\"property\":\"avg\", \"operator\":\"eq\",\"value\":\"5\"}]", "Avg of Last 5 rounds"));
        valuePairs.add(new ExtKeyValuePair("[{\"property\":\"avg\", \"operator\":\"eq\",\"value\":\"3\"}]", "Avg of Last 3 rounds"));
        valuePairs.add(new ExtKeyValuePair("[{\"property\":\"points_per_round\", \"operator\":\"eq\",\"value\":\"all\"}]", "Points per round"));

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

    @OnClick({R.id.ivMenu})
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
        presenter.getPlayerStatistic(player.getId(), keyValuePairKey.getKey());
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
