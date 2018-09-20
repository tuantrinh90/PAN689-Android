package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.util.DateTimeUtils;
import com.football.events.LineupEvent;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import butterknife.OnClick;

public class LineupTransferFragment extends LineUpFragment<ILineupTransferView, ILineupTransferPresenter<ILineupTransferView>> implements ILineupTransferView {

    public static LineupTransferFragment newInstance(LeagueResponse league, Integer teamId) {
        LineupTransferFragment fragment = new LineupTransferFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @NonNull
    @Override
    public ILineupTransferPresenter<ILineupTransferView> createPresenter() {
        return new LineupTransferPresenter(getAppComponent());
    }

    @Override
    protected void initView() {
        super.initView();
        setupTransferMode();
    }

    private void setupTransferMode() {
        boolean isSetupTime = league.getStatus().equals(LeagueResponse.WAITING_FOR_START);
        if (isSetupTime) {
            lineupView.setEditable(true);
            lineupView.setAddable(true);
            lineupView.setRemovable(true);
            tvTimeLabel.setText(R.string.team_setup_time);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getTeamSetUpCalendar(), Constant.FORMAT_DATE_TIME));
            enableCompleteButton(false);

        } else {
            lineupView.setEditable(false);
            lineupView.setAddable(true);
            lineupView.setRemovable(false);
            tvTimeLabel.setText(R.string.start_time);
            tvTime.setText(DateTimeUtils.convertCalendarToString(league.getStartAtCalendar(), Constant.FORMAT_DATE_TIME));

            // hide complete button
            tvComplete.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayTeamState(TeamResponse team) {
        // display budget
        tvBudget.setText(getString(R.string.money_prefix, team.getCurrentBudgetValue()));
    }

    @Override
    public void enableCompleteButton(boolean enable) {
        // disable button complete
        boolean realEnable = AppUtilities.isSetupTime(league.getTeamSetup()) && enable;
        tvComplete.setEnabled(realEnable);
        tvComplete.setBackgroundResource(realEnable ? R.drawable.bg_button_yellow : R.drawable.bg_button_gray);
    }

    @Override
    public void checkCompleted(boolean firstCheck) {
        enableCompleteButton(lineupView.isSetupComplete() || (firstCheck && league != null && league.getTeam().getCompleted()));
    }

    @Override
    public void onCompleteLineup() {
        bus.send(new TeamEvent(null));

        // open viewpager page TeamFragment
        bus.send(new LineupEvent());

//        mActivity.finish();
    }

    @OnClick(R.id.tvComplete)
    public void onCompleteClicked() {
        showMessage(R.string.message_confirm_complete, R.string.yes, R.string.no,
                aVoid -> presenter.completeLineup(teamId), null);
    }

}
