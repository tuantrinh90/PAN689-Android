package com.football.fantasy.fragments.leagues.player_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.google.android.flexbox.FlexboxLayout;

import butterknife.BindView;

public class PlayerDetailFragment extends BaseMainMvpFragment<IPlayerDetailView, IPlayerDetailPresenter<IPlayerDetailView>> implements IPlayerDetailView {

    private static final String KEY_PLAYER = "PLAYER";

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
    @BindView(R.id.tvBallsRecovered)
    ExtTextView tvBallsRecovered;
    @BindView(R.id.tvFoulsCommitted)
    ExtTextView tvFoulsCommitted;

    private PlayerResponse player;

    public static Bundle newBundle(PlayerResponse player) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PLAYER, player);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.player_detail_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
    }

    private void getDataFromBundle() {
        player = (PlayerResponse) getArguments().getSerializable(KEY_PLAYER);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    @Override
    public IPlayerDetailPresenter<IPlayerDetailView> createPresenter() {
        return new PlayerDetailPresenter(getAppComponent());
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
}
