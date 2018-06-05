package com.football.fantasy.fragments.leagues.my_supper_team;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;

import butterknife.BindView;

public class MySupperTeamFragment extends BaseMainMvpFragment<IMySupperTeamView, IMySupperTeamPresenter<IMySupperTeamView>> implements IMySupperTeamView {
    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.ivEdit)
    ImageView ivEdit;
    @BindView(R.id.tvName)
    ExtTextView tvName;
    @BindView(R.id.tvOwner)
    ExtTextView tvOwner;
    @BindView(R.id.ivAvatar)
    CircleImageViewApp ivAvatar;
    @BindView(R.id.tvRank)
    ExtTextView tvRank;
    @BindView(R.id.tvPoints)
    ExtTextView tvPoints;
    @BindView(R.id.tvBudget)
    ExtTextView tvBudget;
    @BindView(R.id.llTeamLineUp)
    LinearLayout llTeamLineUp;
    @BindView(R.id.llTransfer)
    LinearLayout llTransfer;
    @BindView(R.id.llTeamSquad)
    LinearLayout llTeamSquad;
    @BindView(R.id.llStatistics)
    LinearLayout llStatistics;

    @Override
    public int getResourceId() {
        return R.layout.my_supper_team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public IMySupperTeamPresenter<IMySupperTeamView> createPresenter() {
        return new MySupperTeamPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.league_details;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }
}
