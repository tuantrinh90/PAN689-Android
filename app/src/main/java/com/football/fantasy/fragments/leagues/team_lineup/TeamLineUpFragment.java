package com.football.fantasy.fragments.leagues.team_lineup;

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
import com.bon.share_preferences.AppPreferences;
import com.football.adapters.TeamLineupPlayerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.team_lineup.dialog.SelectDialog;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.google.android.flexbox.AlignContent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class TeamLineUpFragment extends BaseMainMvpFragment<ITeamLineUpView, ITeamLineUpPresenter<ITeamLineUpView>> implements ITeamLineUpView {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_TEAM = "TEAM";

    private static final String[] PITCH_VIEWS = new String[]{"4-4-2", "4-3-3", "3-5-2", "3-4-3", "5-3-2", "5-4-1"};

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.tvPitch)
    ExtTextView tvPitch;
    @BindView(R.id.lineupView)
    LineupView lineupView;
    @BindView(R.id.rv_minor_player)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private List<ExtKeyValuePair> valuePairs;
    private String pitchSelect;

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
        pitchSelect = valuePairs.get(0).getKey();

    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
    }

    private void initView(boolean owner) {
        lineupView.setEditable(owner);
        lineupView.setAddable(owner);
        lineupView.setRemovable(owner);
        lineupView.setJustifyContent(AlignContent.SPACE_AROUND);
        lineupView.setAddCallback((position, order) -> {
            List<PlayerResponse> players =
                    StreamSupport.stream(rvPlayer.getAdapter().getDataSet())
                            .filter(predicate -> {
                                Integer mainPos = predicate.getMainPosition();
                                Integer minorPos = predicate.getMinorPosition();
                                return (mainPos != null && mainPos.equals(position)) || (minorPos != null && minorPos.equals(position));
                            })
                            .collect(Collectors.toList());
            new SelectDialog()
                    .setPlayers(players)
                    .setClickCallback(player -> {
                        presenter.addPlayerToPitchView(team.getId(), player, position, order);
                    })
                    .show(getChildFragmentManager(), null);
        });
        tvPitch.setText(pitchSelect);

        TeamLineupPlayerAdapter adapter = new TeamLineupPlayerAdapter(
                new ArrayList<>(),
                player -> AloneFragmentActivity.with(this)
                        .parameters(PlayerDetailFragment.newBundle(
                                player,
                                getString(R.string.team_line_up), false))
                        .start(PlayerDetailFragment.class));
        rvPlayer
                .adapter(adapter)
                .layoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
                .build();
    }

    private void getPitchView() {
        presenter.getPitchView(team.getId(), pitchSelect);
    }

    @NonNull
    @Override
    public ITeamLineUpPresenter<ITeamLineUpView> createPresenter() {
        return new TeamLineUpPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @OnClick(R.id.ivArrow)
    public void onArrowClicked() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(pitchSelect)
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (extKeyValuePair != null && !TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        pitchSelect = extKeyValuePair.getKey();
                        tvPitch.setText(extKeyValuePair.getValue());

                        lineupView.setSquad(pitchSelect);
                        lineupView.notifyDataSetChanged();
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
    public void displayMainPlayers(List<PlayerResponse> players) {
        while (players.size() < 11) {
            players.add(null);
        }
        PlayerResponse[] array = new PlayerResponse[players.size()];
        players.toArray(array);
        lineupView.setupLineup(array, pitchSelect);
    }

    @Override
    public void displayMinorPlayers(List<PlayerResponse> players) {
        rvPlayer.addItems(players);
    }

    @Override
    public void onAddPlayer(PlayerResponse player, Integer position, Integer order) {
        lineupView.addPlayer(player, position, order);
    }
}
