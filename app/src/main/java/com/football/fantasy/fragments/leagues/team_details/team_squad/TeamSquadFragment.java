package com.football.fantasy.fragments.leagues.team_details.team_squad;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.TeamSquadAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.TradeRequestFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamSquadResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_CANCELLED;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_INVALID;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_REJECTED;

public class TeamSquadFragment extends BaseMvpFragment<ITeamSquadView, ITeamSquadPresenter<ITeamSquadView>> implements ITeamSquadView {

    private static final String KEY_TEAM_NAME = "TEAM_NAME";
    private static final String KEY_MY_TEAM_ID = "MY_TEAM_ID";
    private static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_LEAGUE_STATUS = "STATUS";
    private static final String KEY_ACTION = "ACTION";

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

    private int myTeamId;
    private int teamId;
    private String teamName;
    private String title;
    private int leagueStatus;
    private String action;

    private TeamSquadResponse teamSquad;

    TeamSquadAdapter teamSquadAdapter;

    private List<ExtKeyValuePair> properties;
    private ExtKeyValuePair currentProperty;
    private List<ExtKeyValuePair> directions;
    private ExtKeyValuePair currentDirection;

    public static Bundle newBundle(String title, int myTeamId, int teamId, String teamName, int leagueStatus, String action) {
        Bundle bundle = newBundle(title, myTeamId, teamId, teamName, leagueStatus);
        bundle.putString(KEY_ACTION, action);
        return bundle;
    }

    public static Bundle newBundle(String title, int myTeamId, int teamId, String teamName, int leagueStatus) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TEAM_NAME, teamName);
        bundle.putInt(KEY_MY_TEAM_ID, myTeamId);
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_LEAGUE_STATUS, leagueStatus);
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
        title = getArguments().getString(KEY_TITLE);
        myTeamId = getArguments().getInt(KEY_MY_TEAM_ID);
        teamId = getArguments().getInt(KEY_TEAM_ID);
        teamName = getArguments().getString(KEY_TEAM_NAME);
        leagueStatus = getArguments().getInt(KEY_LEAGUE_STATUS);
        action = getArguments().getString(KEY_ACTION);
    }

    void initViews(String gamePlayOption) {
        // setVisible Trade button
        llTrade.setVisibility(gamePlayOption.equals(GAMEPLAY_OPTION_TRANSFER) || leagueStatus == LeagueResponse.FINISHED ? View.INVISIBLE : View.VISIBLE);

        teamSquadAdapter = new TeamSquadAdapter(
                getContext(),
                player -> {
                    PlayerDetailFragment.start(this,
                            player.getId(),
                            teamId,
                            getString(R.string.team_squad),
                            gamePlayOption);
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
        presenter.getTeamSquad(teamId, currentProperty.getKey(), currentDirection.getKey());
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
        TradeRequestFragment.start(this, getString(R.string.team_squad), myTeamId, teamId, teamName, teamSquad, -1);
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
                .title("Select type")
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
    public void displayViews(String gameplayOption) {
        // chỉ initView 1 lần thôi
        if (teamSquad == null) {
            initViews(gameplayOption);
        }
    }

    @Override
    public void displayTeamSquad(TeamSquadResponse response) {
        teamSquad = response;
        rvPlayer.addItems(response.getPlayers());
    }

    @Override
    public void handleAction() {
        switch (action) {
            case TRADE_PROPOSAL_CANCELLED:
            case TRADE_PROPOSAL_INVALID:
                TradeRequestFragment.start(this, getString(R.string.team_squad), myTeamId, teamId, teamName, teamSquad, TradeRequestFragment.REQUEST_TO_YOU_INDEX);
                break;
            case TRADE_PROPOSAL_REJECTED:
                TradeRequestFragment.start(this, getString(R.string.team_squad), myTeamId, teamId, teamName, teamSquad, TradeRequestFragment.REQUEST_BY_YOU_INDEX);
                break;

            default:
                TradeRequestFragment.start(this, getString(R.string.team_squad), myTeamId, teamId, teamName, teamSquad, TradeRequestFragment.REQUEST_TO_YOU_INDEX);
                if (BuildConfig.DEBUG) {
                    Toast.makeText(mActivity, "Mặc định mở REQUEST_TO_YOU", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
