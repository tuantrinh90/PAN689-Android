package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PropsPlayerResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public abstract class LineUpPresenter<V extends ILineUpView> extends BaseDataPresenter<V> implements ILineUpPresenter<V> {

    protected abstract void setLineup(LineupResponse response);

    protected abstract void addPlaySuccess(PropsPlayerResponse response, PlayerResponse player, int position, int order);

    protected abstract void removePlayerSuccess(PropsPlayerResponse response, PlayerResponse player, int position);

    protected abstract void completeLineupSuccess();

    /**
     * @param appComponent
     */
    public LineUpPresenter(AppComponent appComponent) {
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
                            setLineup(response);
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
                    dataModule.getApiService().addPlayer(player.getId(),
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("team_id", String.valueOf(teamId))
                                    .addFormDataPart("player_id", String.valueOf(player.getId()))
                                    .addFormDataPart("order", String.valueOf(order))
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
                            player.getId(),
                            new MultipartBody.Builder()
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
                            removePlayerSuccess(response, player, position);
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
                            completeLineupSuccess();
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
