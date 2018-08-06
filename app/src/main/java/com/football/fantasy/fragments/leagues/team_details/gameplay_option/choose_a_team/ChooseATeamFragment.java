package com.football.fantasy.fragments.leagues.team_details.gameplay_option.choose_a_team;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.TeamSelectAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.gameplay_option.proposal.ProposalFragment;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class ChooseATeamFragment extends BaseMvpFragment<IChooseATeamView, IChooseATeamPresenter<IChooseATeamView>> implements IChooseATeamView {

    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    private static final String KEY_MY_TEAM_ID = "MY_TEAM_ID";


    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvDone)
    ExtTextView tvDone;
    @BindView(R.id.rv_team)
    ExtRecyclerView<TeamResponse> rvTeam;
    @BindView(R.id.buttonProceed)
    ExtTextView buttonProceed;

    private int leagueId;
    private int myTeamId;

    public static void start(Context context, int leagueId, int myTeamId) {
        AloneFragmentActivity.with(context)
                .parameters(ChooseATeamFragment.newBundle(leagueId, myTeamId))
                .start(ChooseATeamFragment.class);
    }

    private static Bundle newBundle(int leagueId, int myTeamId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        bundle.putInt(KEY_MY_TEAM_ID, myTeamId);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_choose_a_team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
        getTeams();
    }

    private void getDataFromBundle() {
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
        myTeamId = getArguments().getInt(KEY_MY_TEAM_ID);
    }

    @NonNull
    @Override
    public IChooseATeamPresenter<IChooseATeamView> createPresenter() {
        return new ChooseATeamDataPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    private void initView() {
        tvDone.setVisibility(View.GONE);
        setEnableMakeProceedButton(false);

        TeamSelectAdapter adapter = new TeamSelectAdapter(
                getContext(),
                aVoid -> {
                    // set visibility view
                    tvDone.setVisibility(View.VISIBLE);
                    setEnableMakeProceedButton(true);
                });

        rvTeam
                .adapter(adapter)
                .build();
    }

    private void getTeams() {
        presenter.getTeams(leagueId);
    }

    private void setEnableMakeProceedButton(boolean enable) {
        buttonProceed.setEnabled(enable);
        buttonProceed.setBackgroundResource(enable ? R.drawable.bg_button_yellow : R.drawable.bg_button_white);
    }

    @OnClick({R.id.tvCancel, R.id.tvDone, R.id.buttonProceed})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                mActivity.finish();
                break;

            case R.id.tvDone:
                TeamResponse team = ((TeamSelectAdapter) rvTeam.getAdapter()).getTeamSelected();
                ProposalFragment.start(getContext(), myTeamId, team.getId());
                mActivity.finish();
                break;

            case R.id.buttonProceed:

                break;
        }
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        // filter other team
        rvTeam.addItems(StreamSupport.stream(teams).filter(team -> team.getId() != myTeamId).collect(Collectors.toList()));
    }
}
