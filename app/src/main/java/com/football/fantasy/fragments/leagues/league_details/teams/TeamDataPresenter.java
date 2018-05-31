package com.football.fantasy.fragments.leagues.league_details.teams;

import android.util.Log;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import java.util.List;

public class TeamDataPresenter extends BaseDataPresenter<ITeamView> implements ITeamPresenter<ITeamView> {
    /**
     * @param appComponent
     */
    public TeamDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeams(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getTeams(leagueId),
                    new ApiCallback<List<TeamResponse>>() {
                        @Override
                        public void onSuccess(List<TeamResponse> teams) {
                            v.displayTeams(teams);
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
    public void removeTeam(int leagueId, Integer teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().removeTeam(leagueId, teamId),
                    new ApiCallback<Object>() {
                        @Override
                        public void onSuccess(Object object) {
                            v.removeSuccess(leagueId);
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
