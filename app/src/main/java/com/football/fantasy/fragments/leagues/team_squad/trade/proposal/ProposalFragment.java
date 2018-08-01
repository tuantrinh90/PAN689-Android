package com.football.fantasy.fragments.leagues.team_squad.trade.proposal;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_squad.trade.proposal_team_squad.ProposalTeamSquadFragment;
import com.football.models.responses.TeamResponse;

import butterknife.BindView;
import butterknife.OnClick;

public class ProposalFragment extends BaseMvpFragment<IProposalView, IProposalPresenter<IProposalView>> implements IProposalView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvTitleTeam1)
    ExtTextView tvTitleTeam1;
    @BindView(R.id.image_player11)
    ImageView imagePlayer11;
    @BindView(R.id.image_player12)
    ImageView imagePlayer12;
    @BindView(R.id.image_player13)
    ImageView imagePlayer13;
    @BindView(R.id.tvTitleTeam2)
    ExtTextView tvTitleTeam2;
    @BindView(R.id.image_player21)
    ImageView imagePlayer21;
    @BindView(R.id.image_player22)
    ImageView imagePlayer22;
    @BindView(R.id.image_player23)
    ImageView imagePlayer23;

    private TeamResponse team;
    private int leagueId;

    public static void start(Context context, TeamResponse team, int leagueId) {
        AloneFragmentActivity.with(context)
                .parameters(ProposalFragment.newBundle(team, leagueId))
                .start(ProposalFragment.class);
    }

    private static Bundle newBundle(TeamResponse team, int leagueId) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_proposal_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
    }

    @NonNull
    @Override
    public IProposalPresenter<IProposalView> createPresenter() {
        return new ProposalDataPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    private void initView() {
    }

    @OnClick({R.id.tvCancel, R.id.image_player11, R.id.image_player12, R.id.image_player13,
            R.id.image_player21, R.id.image_player22, R.id.image_player23})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                mActivity.finish();
                break;
            case R.id.image_player11:
                ProposalTeamSquadFragment.start(getContext(), team, leagueId);
                break;
            case R.id.image_player12:
                ProposalTeamSquadFragment.start(getContext(), team, leagueId);
                break;
            case R.id.image_player13:
                ProposalTeamSquadFragment.start(getContext(), team, leagueId);
                break;
            case R.id.image_player21:
                ProposalTeamSquadFragment.start(getContext(), team, leagueId);
                break;
            case R.id.image_player22:
                ProposalTeamSquadFragment.start(getContext(), team, leagueId);
                break;
            case R.id.image_player23:
                ProposalTeamSquadFragment.start(getContext(), team, leagueId);
                break;
        }
    }
}
