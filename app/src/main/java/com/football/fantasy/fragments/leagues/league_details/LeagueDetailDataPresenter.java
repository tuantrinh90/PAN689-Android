package com.football.fantasy.fragments.leagues.league_details;

import android.util.Log;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.StopResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

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

    @Override
    public void stopLeague(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().stopLeague(leagueId, new MultipartBody.Builder()
                            .setType(MultipartBody.FORM)
                            .addFormDataPart("_method", "DELETE")
                            .build()),
                    new ApiCallback<StopResponse>() {
                        @Override
                        public void onSuccess(StopResponse response) {
                            v.stopLeagueSuccess();
                        }

                        @Override
                        public void onError(String e) {
                            Log.e("eee", e);
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void leaveLeague(int leagueId) {

    }
}
