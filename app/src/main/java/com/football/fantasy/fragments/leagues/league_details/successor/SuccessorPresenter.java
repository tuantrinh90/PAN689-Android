package com.football.fantasy.fragments.leagues.league_details.successor;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.StopResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import java.util.List;

import okhttp3.MultipartBody;

public class SuccessorPresenter extends BaseDataPresenter<ISuccessorView> implements ISuccessorPresenter<ISuccessorView> {
    /**
     * @param appComponent
     */
    public SuccessorPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeams(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTeams(leagueId),
                    new ApiCallback<List<TeamResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(List<TeamResponse> teams) {
                            v.displayTeams(teams);
                        }

                        @Override
                        public void onError(String e) {
                            v.displayTeams(null);
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void leaveLeague(int leagueId, int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v, dataModule.getApiService().leaveLeagues(leagueId, new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("team_id", String.valueOf(teamId)).build()), new ApiCallback<Object>() {
                @Override
                public void onStart() {
                    v.showLoading(true);
                }

                @Override
                public void onComplete() {
                    v.showLoading(false);
                }

                @Override
                public void onSuccess(Object o) {
                    v.onLeaveLeague();
                }

                @Override
                public void onError(String error) {
                    v.showMessage(error);
                }
            }));
        });
    }

    @Override
    public void stopLeague(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().stopLeague(leagueId, new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("_method", "DELETE")
                            .build()),
                    new ApiCallback<StopResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(StopResponse response) {
                            v.onLeaveLeague();
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
