package com.football.fantasy.fragments.leagues.team_details.team_lineup;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.TeamLineupPlayerAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.team_details.team_lineup.dialog.SelectDialog;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.google.android.flexbox.AlignContent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class TeamLineupFragment extends BaseMvpFragment<ITeamLineupView, ITeamLineupPresenter<ITeamLineupView>> implements ITeamLineupView {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TEAM = "TEAM";

    private static final int TEAM_PLAYER_SIZE = 11;

    private static final String[] PITCH_VIEWS = new String[]{"4-4-2", "4-3-3", "3-5-2", "3-4-3", "5-3-2", "5-4-1"};

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.tvPitch)
    ExtTextView tvPitch;
    @BindView(R.id.ivArrow)
    View ivArrow;
    @BindView(R.id.formation)
    View formation;
    @BindView(R.id.lineupView)
    LineupView lineupView;
    @BindView(R.id.rv_minor_player)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private List<ExtKeyValuePair> valuePairs;
    private String formationValue;

    private TeamResponse team;
    private String title;

    public static Bundle newBundle(String title, TeamResponse team) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_TEAM, team);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_lineup_fragment;
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initData();
        initView(AppUtilities.isOwner(getContext(), team.getUserId()));

        getPitchView();
    }

    private void initData() {
        valuePairs = new ArrayList<>();
        for (String pitchView : PITCH_VIEWS) {
            valuePairs.add(new ExtKeyValuePair(pitchView, pitchView));
        }
        formationValue = valuePairs.get(0).getKey();

    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
    }

    private void initView(boolean owner) {
        lineupView.setEditable(owner);
        lineupView.setAddable(owner);
        lineupView.setRemovable(false);
        ivArrow.setVisibility(owner ? View.VISIBLE : View.GONE);
        formation.setEnabled(owner);

        lineupView.setJustifyContent(AlignContent.SPACE_AROUND);
        lineupView.setAddCallback((playerView, position, order) -> handlePlayerClicked(null, position, order));
        lineupView.setEditCallback(this::handlePlayerClicked);
        lineupView.setFormation(formationValue);
        lineupView.setPlayers(new PlayerResponse[TEAM_PLAYER_SIZE]);
        lineupView.notifyDataSetChanged();
        tvPitch.setText(formationValue);

        TeamLineupPlayerAdapter adapter = new TeamLineupPlayerAdapter(
                getContext(),
                new ArrayList<>(),
                player -> {
                    PlayerDetailFragment.start(this,
                            player.getId(),
                            team.getId(),
                            getString(R.string.player_list),
                            GAMEPLAY_OPTION_TRANSFER);
                });
        rvPlayer
                .adapter(adapter)
                .layoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
                .build();
    }

    private void handlePlayerClicked(PlayerResponse fromPlayer, int position, int order) {
        List<PlayerResponse> players =
                StreamSupport.stream(rvPlayer.getAdapter().getDataSet())
                        .filter(predicate -> {
                            Integer mainPos = predicate.getMainPosition();
                            Integer minorPos = predicate.getMinorPosition();
                            return mainPos.equals(position) || minorPos.equals(position);
                        })
                        .collect(Collectors.toList());
        new SelectDialog()
                .setPlayers(players)
                .setClickCallback(player -> {
                    // currentTime > deadline // todo: chờ tái hiện 544
                    Calendar currentTime = Calendar.getInstance();
                    Calendar deadline = team.getCurrentRound() != null ? team.getCurrentRound().getTransferDeadlineCalendar() : null;
                    if (currentTime.before(deadline)) {
                        presenter.addPlayerToPitchView(team.getId(), team.getRound(), fromPlayer, player, position, order);
                    } else {
                        showMessage(R.string.message_can_not_change_position_after_real_match_start, R.string.ok, null);
                    }
                })
                .show(getChildFragmentManager(), null);
    }

    private void getPitchView() {
        presenter.getPitchView(team.getId(), formationValue);
    }

    @NonNull
    @Override
    public ITeamLineupPresenter<ITeamLineupView> createPresenter() {
        return new TeamLineupPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @OnClick(R.id.formation)
    public void onArrowClicked() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(formationValue)
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (extKeyValuePair != null && !TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        formationValue = extKeyValuePair.getKey();
                        tvPitch.setText(extKeyValuePair.getValue());

                        lineupView.setPlayers(new PlayerResponse[TEAM_PLAYER_SIZE]);
                        lineupView.setFormation(formationValue);
                        lineupView.notifyDataSetChanged();

                        presenter.changeTeamFormation(team, formationValue);
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (isLoading) {
            rvPlayer.startLoading();
        } else {
            rvPlayer.stopLoading();
        }
    }

    @Override
    public void displayTeam(TeamResponse team) {
        if (!TextUtils.isEmpty(team.getFormation()) || team.getFormation().equals("null")) {
            lineupView.setFormation(team.getFormation());
            lineupView.notifyDataSetChanged();
        }
    }

    @Override
    public void displayMainPlayers(List<PlayerResponse> players) {
        while (players.size() < TEAM_PLAYER_SIZE) {
            players.add(null);
        }
        for (PlayerResponse player : players) {
            if (player != null) {
                lineupView.addPlayer(player, player.getPosition(), player.getOrder());
            }
        }
    }

    @Override
    public void displayMinorPlayers(List<PlayerResponse> players) {
        rvPlayer.clear();
        rvPlayer.addItems(players);
    }

    @Override
    public void onAddPlayer(PlayerResponse fromPlayer, PlayerResponse toPlayer, Integer position, Integer order) {
        lineupView.addPlayer(toPlayer, position, order);
        rvPlayer.removeItem(toPlayer);
        if (fromPlayer != null) {
            rvPlayer.addItem(fromPlayer);
        }
    }
}
