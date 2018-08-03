package com.football.fantasy.fragments.leagues.team_squad.trade.proposal_team_squad;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.football.adapters.TeamSquadAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.edittext_app.EditTextApp;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.PlayerEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.models.responses.PlayerResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ProposalTeamSquadFragment extends BaseMvpFragment<IProposalTeamSquadView, IProposalTeamSquadPresenter<IProposalTeamSquadView>> implements IProposalTeamSquadView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_PLAYER_INDEX = "PLAYER_INDEX";

    @BindView(R.id.tvSortByColumn)
    EditTextApp tvSortByColumn;
    @BindView(R.id.tvSortByValue)
    EditTextApp tvSortByValue;
    @BindView(R.id.rv_player)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private int teamId;
    private int playerIndex;

    private List<ExtKeyValuePair> properties;
    private ExtKeyValuePair currentProperty;
    private List<ExtKeyValuePair> directions;
    private ExtKeyValuePair currentDirection;

    public static void start(Context context, int teamId, int playerIndex) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalTeamSquadFragment.newBundle(teamId, playerIndex))
                .start(ProposalTeamSquadFragment.class);
    }

    private static Bundle newBundle(int teamId, int playerIndex) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TEAM, teamId);
        bundle.putInt(KEY_PLAYER_INDEX, playerIndex);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_proposal_team_squad_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
        initData();
        getPlayers();
    }

    private void getDataFromBundle() {
        teamId = getArguments().getInt(KEY_TEAM);
        playerIndex = getArguments().getInt(KEY_PLAYER_INDEX);
    }

    @NonNull
    @Override
    public IProposalTeamSquadPresenter<IProposalTeamSquadView> createPresenter() {
        return new ProposalDataTeamSquadPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.trade_proposal;
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

    private void displaySort() {
        tvSortByColumn.setContent(currentProperty.getValue());
        tvSortByValue.setContent(currentDirection.getValue());
    }

    private void initView() {
        TeamSquadAdapter adapter = new TeamSquadAdapter(
                getContext(),
                player -> {
                    PlayerDetailFragment.start(this, player, getString(R.string.team_squad), false);
                });
        adapter.setAddCallback(
                player -> {
                    bus.send(new PlayerEvent.Builder()
                            .action(PlayerEvent.ACTION_ADD_CLICK)
                            .position(playerIndex)
                            .data(player)
                            .build());
                    getActivity().finish();
                });
        rvPlayer
                .adapter(adapter)
                .refreshListener(() -> {
                    rvPlayer.clear();
                    getPlayers();
                })
                .build();

    }

    private void getPlayers() {
        presenter.getLineup(teamId, currentProperty.getKey(), currentDirection.getKey());
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
                        getPlayers();
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
                        getPlayers();
                        rvPlayer.clear();
                        rvPlayer.startLoading();
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void displayPlayers(List<PlayerResponse> players) {
        rvPlayer.addItems(players);
    }
}
