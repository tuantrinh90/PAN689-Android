package com.football.fantasy.fragments.leagues.league_details;

import android.util.Log;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LeagueResponse;
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
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getLeagueDetail(leagueId),
                    new ApiCallback<LeagueResponse>() {
                        @Override
                        public void onSuccess(LeagueResponse response) {
                            v.displayLeague(response);
                        }

                        @Override
                        public void onError(String e) {
                            Log.e("eee", e);
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
