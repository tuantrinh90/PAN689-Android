package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LineupResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class LineUpPresenter extends BaseDataPresenter<ILineUpView> implements ILineUpPresenter<ILineUpView> {

    /**
     * @param appComponent
     */
    public LineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLineup(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getLineup(leagueId),
                    new ApiCallback<LineupResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(LineupResponse response) {
                            v.displayLineup(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void addPlayer(int teamId, Integer playerId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().addPlayer(new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("team_id", String.valueOf(teamId))
                            .addFormDataPart("player_id", String.valueOf(playerId))
                            .build()),
                    new ApiCallback<PlayerResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PlayerResponse response) {
                            v.onAddPlayer(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
