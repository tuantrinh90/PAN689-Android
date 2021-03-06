package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_team_squad;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
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
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class ProposalTeamSquadFragment extends BaseMvpFragment<IProposalTeamSquadView, IProposalTeamSquadPresenter<IProposalTeamSquadView>> implements IProposalTeamSquadView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_TEAM_NAME = "TEAM_NAME";
    private static final String KEY_PLAYER_INDEX = "PLAYER_INDEX";
    private static final String KEY_IDS = "IDS";

    private static final String KEY_NAME = "NAME";
    private static final String KEY_MAIN_POSITION = "MAIN_POSITION";


    @BindView(R.id.tvHeader)
    ExtTextView tvHeader;
    @BindView(R.id.tvSortByColumn)
    EditTextApp tvSortByColumn;
    @BindView(R.id.tvSortByValue)
    EditTextApp tvSortByValue;
    @BindView(R.id.rv_player)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private int teamId;
    private String teamName;
    private int playerIndex;
    private ArrayList<Integer> ids;

    private List<ExtKeyValuePair> properties;
    private ExtKeyValuePair currentProperty;
    private List<ExtKeyValuePair> directions;
    private ExtKeyValuePair currentDirection;

    public static void start(Context context, int teamId, String teamName, int playerIndex, ArrayList<Integer> ids) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalTeamSquadFragment.newBundle(teamId, teamName, playerIndex, ids))
                .start(ProposalTeamSquadFragment.class);
    }

    private static Bundle newBundle(int teamId, String teamName, int playerIndex, ArrayList<Integer> ids) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TEAM, teamId);
        bundle.putString(KEY_TEAM_NAME, teamName);
        bundle.putInt(KEY_PLAYER_INDEX, playerIndex);
        bundle.putIntegerArrayList(KEY_IDS, ids);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_team_details_gameplay_option_proposal_team_squad_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
        initData();
        rvPlayer.startLoading();
        getPlayers();
    }

    private void getDataFromBundle() {
        teamId = getArguments().getInt(KEY_TEAM);
        teamName = getArguments().getString(KEY_TEAM_NAME);
        playerIndex = getArguments().getInt(KEY_PLAYER_INDEX);
        ids = getArguments().getIntegerArrayList(KEY_IDS);
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
        properties.add(new ExtKeyValuePair("name", getString(R.string.name)));
        properties.add(new ExtKeyValuePair("total_point", getString(R.string.points)));
        properties.add(new ExtKeyValuePair("main_position", getString(R.string.main_position)));

        directions = new ArrayList<>();
        directions.add(new ExtKeyValuePair("asc", getString(R.string.a_z)));
        directions.add(new ExtKeyValuePair("desc", getString(R.string.z_a)));

        currentProperty = properties.get(0);
        currentDirection = directions.get(0);
        displaySort();
    }

    private void displaySort() {
        tvSortByColumn.setContent(currentProperty.getValue());
        tvSortByValue.setContent(currentDirection.getValue());
    }

    private void initView() {
        tvHeader.setText(teamName);

        TeamSquadAdapter adapter = new TeamSquadAdapter(
                getContext(),
                player -> {
                    PlayerDetailFragment.start(
                            getContext(),
                            player.getId(),
                            teamId,
                            getString(R.string.team_squad),
                            PlayerDetailFragment.PICK_NONE_INFO,
                            GAMEPLAY_OPTION_TRANSFER);
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
        rvPlayer.addItems(StreamSupport.stream(players).filter(player -> !ids.contains(player.getId())).collect(Collectors.toList()));
    }

    @Override
    public void stopLoading() {
        rvPlayer.stopLoading();
    }
}
