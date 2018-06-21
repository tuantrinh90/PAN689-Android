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
import com.football.adapters.TeamLineupPlayerAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.lineup.LineupView;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class TeamLineUpFragment extends BaseMainMvpFragment<ITeamLineUpView, ITeamLineUpPresenter<ITeamLineUpView>> implements ITeamLineUpView {

    private static final String KEY_TEAM = "TEAM";

    private static final String[] PITCH_VIEWS = new String[]{"4-4-2", "4-3-3", "4-3-4", "3-5-2", "3-4-3", "5-3-2", "5-4-1"};

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.tvPitch)
    ExtTextView tvPitch;
    @BindView(R.id.lineupView)
    LineupView lineupView;
    @BindView(R.id.rv_minor_player)
    ExtRecyclerView<PlayerResponse> rvPlayer;

    private List<ExtKeyValuePair> valuePairs;
    private String pitchDefault;

    private TeamResponse team;

    public static Bundle newBundle(TeamResponse team) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_lineup_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initData();
        initView();

        getPitchView();
    }

    private void initData() {
        valuePairs = new ArrayList<>();
        for (String pitchView : PITCH_VIEWS) {
            valuePairs.add(new ExtKeyValuePair(pitchView, pitchView));
        }
        pitchDefault = valuePairs.get(0).getKey();

    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
    }

    private void initView() {
        tvPitch.setText(pitchDefault);

        TeamLineupPlayerAdapter adapter = new TeamLineupPlayerAdapter(new ArrayList<>());
        rvPlayer
                .adapter(adapter)
                .autoMeasureEnable(true)
                .layoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false))
                .build();
    }

    private void getPitchView() {
        presenter.getPitchView(team.getId(), pitchDefault);
    }

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
                .setValue(pitchDefault)
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (extKeyValuePair != null && !TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        pitchDefault = extKeyValuePair.getKey();
                        tvPitch.setText(extKeyValuePair.getValue());
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void displayMainPlayers(List<PlayerResponse> players) {
    }

    @Override
    public void displayMinorPlayers(List<PlayerResponse> players) {
        rvPlayer.addItems(players);
    }
}
