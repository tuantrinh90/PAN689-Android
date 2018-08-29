package com.football.fantasy.fragments.leagues.your_team.line_up.transfer;

import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpPresenter;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class LineupTransferDataPresenter extends LineUpPresenter<ILineupTransferView> implements ILineupTransferPresenter<ILineupTransferView> {
    /**
     * @param appComponent
     */
    protected LineupTransferDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLineup(int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getLineup(teamId),
                    new ApiCallback<LineupResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(LineupResponse response) {
                            v.displayTeamState(response.getTeam());
                            v.displayLineupPlayers(response.getPlayers());
                            v.checkCompleted(true);
                            v.displayStatistic(response.getStatistic());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void addPlayer(PlayerResponse player, int teamId, int position, int order) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().addPlayer(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("team_id", String.valueOf(teamId))
                            .addFormDataPart("player_id", String.valueOf(player.getId()))
                            .addFormDataPart("order", String.valueOf(order))
                            .build()),
                    new ApiCallback<PropsPlayerResponse>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(PropsPlayerResponse response) {
                            v.updateStatistic(position == PlayerResponse.POSITION_NONE ? player.getMainPosition() : position, 1);
                            v.handleCallback(true, "");
                            v.displayTeamState(response.getTeam());
                            v.onAddPlayer(response.getTeam(), player, order);
                            v.checkCompleted(false);
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
                    dataModule.getApiService().removePlayer(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("team_id", String.valueOf(teamId))
                            .addFormDataPart("player_id", String.valueOf(player.getId()))
                            .build()),
                    new ApiCallback<PropsPlayerResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(PropsPlayerResponse response) {
                            v.updateStatistic(position, -1);
                            v.displayTeamState(response.getTeam());
                            v.onRemovePlayer(response.getTeam(), player);
                            v.enableCompleteButton(false);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void completeLineup(int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().completeLineup(teamId),
                    new ApiCallback<Object>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(Object response) {
                            v.onCompleteLineup();
                            v.enableCompleteButton(false);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
