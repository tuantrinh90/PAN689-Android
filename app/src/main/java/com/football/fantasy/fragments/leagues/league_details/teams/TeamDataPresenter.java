package com.football.fantasy.fragments.leagues.league_details.teams;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import io.reactivex.disposables.Disposable;

public class TeamDataPresenter extends BaseDataPresenter<ITeamView> implements ITeamPresenter<ITeamView> {

    private Disposable getTeam;

    /**
     * @param appComponent
     */
    public TeamDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeams(int leagueId) {
        if (getTeam != null) {
            getTeam.dispose();
        }

        getOptView().doIfPresent(v -> {
            getTeam = RxUtilities.async(v,
                    dataModule.getApiService().getTeams(leagueId),
                    new ApiCallback<PagingResponse<TeamResponse>>() {
                        @Override
                        public void onComplete() {
                            v.hideLoading();
                        }

                        @Override
                        public void onSuccess(PagingResponse<TeamResponse> response) {
                            v.displayTeams(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    });
            mCompositeDisposable.add(getTeam);
        });
    }

    @Override
    public void removeTeam(int leagueId, Integer teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().removeTeam(leagueId, teamId),
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
                        public void onSuccess(Object object) {
                            v.removeSuccess(teamId);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
