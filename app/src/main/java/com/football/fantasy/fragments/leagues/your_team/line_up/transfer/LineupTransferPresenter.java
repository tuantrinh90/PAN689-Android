package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class LineupTransferPresenter extends LineUpPresenter<ILineupTransferView> implements ILineupTransferPresenter<ILineupTransferView> {
    @Override
    protected void setLineup(LineupResponse response) {
        getOptView().doIfPresent(v -> {
            v.displayTeamState(response.getTeam());
            v.displayLineupPlayers(response.getPlayers());
            if (response.getTeam().getCompleted()) {
                v.enableCompleteButton(false);
            }
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
            v.checkCompleted();
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

    @Override
    public void addPlayer(PlayerResponse player, int teamId, int position, int order) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().addPlayer(
                            teamId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("player_id", String.valueOf(player.getId()))
                                    .addFormDataPart("order", String.valueOf(order))
                                    .build()),
                    new ApiCallback<PropsPlayerResponse>() {
                        @Override
                        public void onSuccess(PropsPlayerResponse response) {
                            addPlaySuccess(response, player, position, order);
                        }

                        @Override
                        public void onError(String error) {
                            v.handleCallback(false, error);
                        }
                    }));
        });
    }

    @Override
    public void removePlayer(PlayerResponse player, int position, int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().removePlayer(
                            teamId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("team_id", String.valueOf(teamId))
                                    .addFormDataPart("player_id", String.valueOf(player.getId()))
                                    .build()),
                    new ApiCallback<PropsPlayerResponse>() {
                        @Override
                        public void onSuccess(PropsPlayerResponse response) {
                            removePlayerSuccess(response, player, position);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
