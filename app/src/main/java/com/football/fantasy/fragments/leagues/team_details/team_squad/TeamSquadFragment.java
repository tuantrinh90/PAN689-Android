package com.football.fantasy.fragments.leagues.team_details.team_squad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.TeamSquadAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.TradeFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TeamSquadResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class TeamSquadFragment extends BaseMvpFragment<ITeamSquadView, ITeamSquadPresenter<ITeamSquadView>> implements ITeamSquadView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.llTrade)
    LinearLayout llTrade;
    @BindView(R.id.tvSortByColumn)
    EditTextApp tvSortByColumn;
    @BindView(R.id.tvSortByValue)
    EditTextApp tvSortByValue;
    @BindView(R.id.rvPlayer)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private TeamResponse team;
    private String title;
    private LeagueResponse league;

    TeamSquadAdapter teamSquadAdapter;

    private List<ExtKeyValuePair> properties;
    private ExtKeyValuePair currentProperty;
    private List<ExtKeyValuePair> directions;
    private ExtKeyValuePair currentDirection;

    public static Bundle newBundle(TeamResponse team, String title, LeagueResponse league) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_LEAGUE, league);
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
        directions.add(new ExtKeyValuePair("asc", "A-Z"));
        directions.add(new ExtKeyValuePair("desc", "Z-A"));

        currentProperty = properties.get(0);
        currentDirection = directions.get(0);
        displaySort();
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        title = getArguments().getString(KEY_TITLE);
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {
        // setVisible Trade button
        llTrade.setVisibility(league.getGameplayOption().equals(GAMEPLAY_OPTION_TRANSFER) ? View.INVISIBLE : View.VISIBLE);

        teamSquadAdapter = new TeamSquadAdapter(
                getContext(),
                player -> {
                    PlayerDetailFragment.start(this,
                            player,
                            team.getId(),
                            getString(R.string.team_squad),
                            league.getGameplayOption());
                });
        rvPlayer
                .adapter(teamSquadAdapter)
                .refreshListener(() -> {
                    rvPlayer.clear();
                    getTeamSquad();
                })
                .build();
    }

    private void displaySort() {
        tvSortByColumn.setContent(currentProperty.getValue());
        tvSortByValue.setContent(currentDirection.getValue());
    }

    private void getTeamSquad() {
        rvPlayer.startLoading();
        presenter.getTeamSquad(team.getId(), currentProperty.getKey(), currentDirection.getKey());
    }

    @NonNull
    @Override
    public ITeamSquadPresenter<ITeamSquadView> createPresenter() {
        return new TeamSquadPresenter(getAppComponent());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvPlayer.stopLoading();
        }
    }

    @OnClick(R.id.llTrade)
    void onClickTrade() {
        TradeFragment.start(this, getString(R.string.team_squad), team, league);
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
                        rvPlayer.clear();
                        rvPlayer.startLoading();
                    }
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
                        rvPlayer.clear();
                        rvPlayer.startLoading();
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void displayTeamSquad(TeamSquadResponse response) {
        rvPlayer.stopLoading();
        rvPlayer.addItems(response.getPlayers());
    }
}
