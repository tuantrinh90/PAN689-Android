package com.football.fantasy.fragments.leagues.team_squad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtPagingListView;
import com.football.adapters.TeamSquadAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamSquadFragment extends BaseMainMvpFragment<ITeamSquadView, ITeamSquadPresenter<ITeamSquadView>> implements ITeamSquadView {
    @BindView(R.id.llTrade)
    LinearLayout llTrade;
    @BindView(R.id.tvSortByColumn)
    EditTextApp tvSortByColumn;
    @BindView(R.id.tvSortByValue)
    EditTextApp tvSortByValue;
    @BindView(R.id.lvData)
    ExtPagingListView lvData;

    List<PlayerResponse> playerResponses;
    TeamSquadAdapter teamSquadAdapter;

    @Override
    public int getResourceId() {
        return R.layout.team_squad_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        playerResponses = new ArrayList<>();
        playerResponses.add(new PlayerResponse(0, null, null, 0, null, "abc", "abc", "http://media.doisongvietnam.vn/u/rootimage/editor/2017/08/15/20/19/w825/2081502781555_4841.jpg",
                true, 0, 3, 1000l));
        playerResponses.add(new PlayerResponse(0, null, null, 0, null, "abc", "abc", "http://media.doisongvietnam.vn/u/rootimage/editor/2017/08/15/20/19/w825/2081502781555_4841.jpg",
                true, 1, 3, 1000l));
        playerResponses.add(new PlayerResponse(0, null, null, 0, null, "abc", "abc", "http://media.doisongvietnam.vn/u/rootimage/editor/2017/08/15/20/19/w825/2081502781555_4841.jpg",
                true, 2, 3, 1000l));
        playerResponses.add(new PlayerResponse(0, null, null, 0, null, "abc", "abc", "http://media.doisongvietnam.vn/u/rootimage/editor/2017/08/15/20/19/w825/2081502781555_4841.jpg",
                true, 3, 3, 1000l));
        playerResponses.add(new PlayerResponse(0, null, null, 0, null, "abc", "abc", "http://media.doisongvietnam.vn/u/rootimage/editor/2017/08/15/20/19/w825/2081502781555_4841.jpg",
                true, 1, 3, 1000l));

        teamSquadAdapter = new TeamSquadAdapter(mActivity, playerResponses);
        lvData.init(mActivity, teamSquadAdapter);
    }

    @Override
    public ITeamSquadPresenter<ITeamSquadView> createPresenter() {
        return new TeamSquadPresenter(getAppComponent());
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

    @OnClick(R.id.llTrade)
    void onClickTrade() {

    }

    @OnClick(R.id.tvSortByColumn)
    void onClickSortByColumn() {

    }

    @OnClick(R.id.tvSortByValue)
    void onClickSortByValue() {

    }
}
