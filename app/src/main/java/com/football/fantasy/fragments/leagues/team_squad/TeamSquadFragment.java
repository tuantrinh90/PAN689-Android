package com.football.fantasy.fragments.leagues.team_squad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.TeamSquadAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TeamSquadResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamSquadFragment extends BaseMainMvpFragment<ITeamSquadView, ITeamSquadPresenter<ITeamSquadView>> implements ITeamSquadView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_TITLE = "TITLE";

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.llTrade)
    LinearLayout llTrade;
    @BindView(R.id.tvSortByColumn)
    EditTextApp tvSortByColumn;
    @BindView(R.id.tvSortByValue)
    EditTextApp tvSortByValue;
    @BindView(R.id.lvData)
    ExtPagingListView lvData;

    private TeamResponse team;
    private String title;
    TeamSquadAdapter teamSquadAdapter;

    private List<ExtKeyValuePair> properties;
    private ExtKeyValuePair currentProperty;
    private List<ExtKeyValuePair> directions;
    private ExtKeyValuePair currentDirection;

    public static Bundle newBundle(TeamResponse team, String title) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putString(KEY_TITLE, title);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        initData();
        getTeamSquad();
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

    private void initData() {
        properties = new ArrayList<>();
        properties.add(new ExtKeyValuePair("name", "Name"));
        properties.add(new ExtKeyValuePair("total_point", "Points"));
        properties.add(new ExtKeyValuePair("main_position", "Main position"));

        directions = new ArrayList<>();
        directions.add(new ExtKeyValuePair("asc", "ASC"));
        directions.add(new ExtKeyValuePair("desc", "DESC"));

        currentProperty = properties.get(0);
        currentDirection = directions.get(0);
        displaySort();
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        title = getArguments().getString(KEY_TITLE);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));

        teamSquadAdapter = new TeamSquadAdapter(mActivity, new ArrayList<>());
        lvData.init(mActivity, teamSquadAdapter)
                .setOnExtRefreshListener(() -> {

                })
                .setOnExtLoadMoreListener(() -> {

                });
    }

    private void displaySort() {
        tvSortByColumn.setContent(currentProperty.getValue());
        tvSortByValue.setContent(currentDirection.getValue());
    }

    private void getTeamSquad() {
        lvData.startLoading(true);
        presenter.getTeamSquad(team.getId(), currentProperty.getKey(), currentDirection.getKey());
    }

    @Override
    public ITeamSquadPresenter<ITeamSquadView> createPresenter() {
        return new TeamSquadPresenter(getAppComponent());
    }

    @OnClick(R.id.llTrade)
    void onClickTrade() {

    }

    @OnClick(R.id.tvSortByColumn)
    void onClickSortByColumn() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(currentProperty.getKey())
                .setExtKeyValuePairs(properties)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (extKeyValuePair != null && !TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        currentProperty = extKeyValuePair;
                        displaySort();
                        getTeamSquad();
                    }
                    displaySort();
                }).show(getFragmentManager(), null);
    }

    @OnClick(R.id.tvSortByValue)
    void onClickSortByValue() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(currentDirection.getKey())
                .setExtKeyValuePairs(directions)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (extKeyValuePair != null && !TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        currentDirection = extKeyValuePair;
                        displaySort();
                        getTeamSquad();
                    }
                    displaySort();
                }).show(getFragmentManager(), null);
    }

    @Override
    public void displayTeamSquad(TeamSquadResponse response) {
        lvData.clearItems();
        lvData.stopLoading(true);
        Optional.from(lvData).doIfPresent(rv -> rv.addNewItems(response.getPlayers()));
    }
}
