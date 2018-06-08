package com.football.fantasy.fragments.leagues.team_statistics;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamStatisticAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.TeamStatisticResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TeamStatisticFragment extends BaseMainMvpFragment<ITeamStatisticView, ITeamStatisticPresenter<ITeamStatisticView>> implements ITeamStatisticView {
    @BindView(R.id.llPointPerPlayer)
    LinearLayout llPointPerPlayer;
    @BindView(R.id.ivNumber)
    ImageView ivNumber;
    @BindView(R.id.tvRankValue)
    ExtTextView tvRankValue;
    @BindView(R.id.tvRankLabel)
    ExtTextView tvRankLabel;
    @BindView(R.id.tvPoints)
    ExtTextView tvPoints;
    @BindView(R.id.tvBudget)
    ExtTextView tvBudget;
    @BindView(R.id.lvData)
    ExtPagingListView lvData;

    List<TeamStatisticResponse> teamStatisticResponses;
    TeamStatisticAdapter teamStatisticAdapter;

    @Override
    public int getResourceId() {
        return R.layout.team_statistic_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initData();
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initData() {
        teamStatisticResponses = new ArrayList<TeamStatisticResponse>() {{
            add(new TeamStatisticResponse("01", "200", "20", false));
            add(new TeamStatisticResponse("02", "200", "20", true));
            add(new TeamStatisticResponse("03", "200", "20", false));
            add(new TeamStatisticResponse("04", "200", "20", true));
        }};
        teamStatisticAdapter = new TeamStatisticAdapter(mActivity, teamStatisticResponses);
        lvData.init(mActivity, teamStatisticAdapter);
    }

    @Override
    public ITeamStatisticPresenter<ITeamStatisticView> createPresenter() {
        return new TeamStatisticPresenter(getAppComponent());
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
