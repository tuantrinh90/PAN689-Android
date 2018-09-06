package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;

public class LineupTransferPresenter extends LineUpPresenter<ILineupTransferView> implements ILineupTransferPresenter<ILineupTransferView> {
    @Override
    protected void onLineupSuccess(LineupResponse response) {
        getOptView().doIfPresent(v -> {
            v.displayTeamState(response.getTeam());
            v.displayLineupPlayers(response.getPlayers());
            v.checkCompleted(true);
            v.displayStatistic(response.getStatistic());
        });
    }

    @Override
    protected void onAddPlaySuccess(PropsPlayerResponse response, PlayerResponse player, int position, int order) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position, 1);
            v.handleCallback(true, "");
            v.displayTeamState(response.getTeam());
            v.onAddPlayer(response.getTeam(), player, order);
            v.checkCompleted(false);
        });
    }

    @Override
    protected void onRemovePlayerSuccess(PropsPlayerResponse response, PlayerResponse player, int position) {
        getOptView().doIfPresent(v -> {
            v.updateStatistic(position, -1);
            v.displayTeamState(response.getTeam());
            v.onRemovePlayer(response.getTeam(), player);
            v.enableCompleteButton(false);
        });
    }

    @Override
    protected void onCompleteLineup() {
        getOptView().doIfPresent(v -> {
            v.onCompleteLineup();
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
