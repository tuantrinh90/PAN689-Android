package com.football.fantasy.fragments.leagues.your_team.team_list;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import java.util.List;

public class TeamListPresenter extends BaseDataPresenter<ITeamListView> implements ITeamListPresenter<ITeamListView> {

    /**
     * @param appComponent
     */
    public TeamListPresenter(AppComponent appComponent) {
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
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
