package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.util.DateTimeUtils;
import com.bon.util.DialogUtils;
import com.football.common.activities.AloneFragmentActivity;
import com.football.customizes.lineup.PlayerView;
import com.football.events.LineupEvent;
import com.football.events.TeamEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailForLineupFragment;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.players_popup.PlayerPopupFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.OnClick;

import static com.football.customizes.lineup.PlayerView.NONE_ORDER;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICK;
import static com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment.PICK_PICKED;

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

        presenter.getLineup(teamId);
    }

    @Override
    protected void onLineupViewAddClicked(PlayerView playerView, int position, int order) {
        boolean isSetupTime = AppUtilities.isSetupTime(league);
        if (isSetupTime) {
            AloneFragmentActivity.with(this)
                    .parameters(PlayerPopupFragment.newBundle(position, order, league))
                    .start(PlayerPopupFragment.class);
        } else {
            showMessage(getString(R.string.message_pick_after_team_setup_time));
        }
    }

    @Override
    protected void onAddClickedFromPopup(PlayerResponse player, int position, int order) {
        if (!lineupView.isFullPosition(position)) {
            if (order == NONE_ORDER) {
                order = lineupView.getOrder(position);
            }
            presenter.addPlayer(player, teamId, position, order);
        } else {
            callback.accept(false, getString(R.string.message_full_position));
        }
    }

    @Override
    protected void onLineupViewRemoveClicked(PlayerResponse player, int position, int index) {
        DialogUtils.messageBox(mActivity,
                0,
                getString(R.string.app_name),
                getString(R.string.message_confirm_remove_lineup_player),
                getString(R.string.ok),
                getString(R.string.cancel),
                (dialog, which) -> presenter.removePlayer(player, position, teamId),
                (dialog, which) -> {
                });
    }

    @Override
    protected void onLineupViewInfoClicked(PlayerResponse player, int position, int order) {
        PlayerDetailForLineupFragment.start(
                this,
                player,
                -1,
                getString(R.string.lineup),
                league.getGameplayOption(),
                player.getSelected() ? PICK_PICKED : PICK_PICK,
                position,
                order);
    }

    private void setupTransferMode() {
        if (AppUtilities.isSetupTime(league)) {
            lineupView.setEditable(true);
            lineupView.setAddable(true);
            lineupView.setRemovable(true);
            tvTimeLabel.setText(R.string.team_setup_time);
            tvTime.setText(league.getTeamSetupFormatted());
            enableCompleteButton(false);

        } else {
            lineupView.setEditable(false);
            lineupView.setAddable(true);
            lineupView.setRemovable(false);
            tvTimeLabel.setText(R.string.start_time);
            tvTime.setText(league.getStartTimeFormatted());

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
        boolean realEnable = AppUtilities.isSetupTime(league) && enable;
        tvComplete.setEnabled(realEnable);
        tvComplete.setBackgroundResource(realEnable ? R.drawable.bg_button_yellow : R.drawable.bg_button_gray);
    }

    @Override
    public void checkCompleted() {
        enableCompleteButton(lineupView.isSetupComplete());
    }

    @Override
    public void completeLineupSuccess() {
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
