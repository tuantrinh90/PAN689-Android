package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;

public class LineupTransferPresenter extends LineUpPresenter<ILineupTransferView> implements ILineupTransferPresenter<ILineupTransferView> {
    @Override
    protected void setLineup(LineupResponse response) {
        getOptView().doIfPresent(v -> {
            v.displayTeamState(response.getTeam());
            v.displayLineupPlayers(response.getPlayers());
            v.checkCompleted(true);
            v.displayStatistic(response.getStatistic());
        });
    }

    @Override
    protected void addPlaySuccess(PropsPlayerResponse response, PlayerResponse player, int position, int order) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position, 1);
            v.handleCallback(true, "");
            v.displayTeamState(response.getTeam());
            v.addPlayerSuccess(response.getTeam(), player, order);
            v.checkCompleted(false);
        });
    }

    @Override
    protected void removePlayerSuccess(PropsPlayerResponse response, PlayerResponse player, int position) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position, -1);
            v.displayTeamState(response.getTeam());
            v.removePlayerSuccess(response.getTeam(), player);
            v.enableCompleteButton(false);
        });
    }

    @Override
    protected void completeLineupSuccess() {
        getOptView().doIfPresent(v -> {
            v.completeLineupSuccess();
            v.enableCompleteButton(false);
        });
    }

    /**
     * @param appComponent
     */
    protected LineupTransferPresenter(AppComponent appComponent) {
        super(appComponent);
    }


}
