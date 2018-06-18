package com.football.fantasy.fragments.leagues.league_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.StopResponse;
import com.football.utilities.RxUtilities;

public class LeagueDetailDataPresenter extends BaseDataPresenter<ILeagueDetailView> implements ILeagueDetailPresenter<ILeagueDetailView> {
    /**
     * @param appComponent
     */
    public LeagueDetailDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLeagueDetail(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getLeagueDetail(leagueId),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(LeagueResponse response) {
                            v.displayLeague(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void stopLeague(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().stopLeague(leagueId),
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
                            v.stopLeagueSuccess();
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

}
