package com.football.fantasy.fragments.leagues.team_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

public class TeamDetailPresenter extends BaseDataPresenter<ITeamDetailView> implements ITeamDetailPresenter<ITeamDetailView> {
    /**
     * @param appComponent
     */
    public TeamDetailPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeamDetails(int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTeamDetails(teamId),
                    new ApiCallback<TeamResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(TeamResponse response) {
                            v.displayTeamDetails(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
