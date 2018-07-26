package com.football.fantasy.fragments.leagues.team_squad.trade.choose_a_team;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.TeamSelectAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseATeamFragment extends BaseMainMvpFragment<IChooseATeamView, IChooseATeamPresenter<IChooseATeamView>> implements IChooseATeamView {

    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";


    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvDone)
    ExtTextView tvDone;
    @BindView(R.id.rv_team)
    ExtRecyclerView<TeamResponse> rvTeam;

    private int leagueId;

    public static void start(Context context, int leagueId) {
        AloneFragmentActivity.with(context)
                .parameters(ChooseATeamFragment.newBundle(leagueId))
                .start(ChooseATeamFragment.class);
    }

    private static Bundle newBundle(int leagueId) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
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

        TeamSelectAdapter adapter = new TeamSelectAdapter(
                getContext(),
                aVoid -> {
                    // set visibility view
                    tvDone.setVisibility(View.VISIBLE);
                });

        rvTeam
                .adapter(adapter)
                .build();
    }

    private void getTeams() {
        presenter.getTeams(leagueId, 1);
    }

    @OnClick({R.id.tvCancel, R.id.tvDone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                mActivity.finish();
                break;
            case R.id.tvDone:
                TeamResponse team = ((TeamSelectAdapter) rvTeam.getAdapter()).getTeamSelected();

                break;
        }
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        rvTeam.addItems(teams);
    }
}
