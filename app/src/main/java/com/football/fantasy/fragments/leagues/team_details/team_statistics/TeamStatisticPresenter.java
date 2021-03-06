package com.football.fantasy.fragments.leagues.team_details.team_statistics;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamStatisticResponse;
import com.football.utilities.RxUtilities;

public class TeamStatisticPresenter extends BaseDataPresenter<ITeamStatisticView> implements ITeamStatisticPresenter<ITeamStatisticView> {
    /**
     * @param appComponent
     */
    public TeamStatisticPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeamStatistic(int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTeamStatistic(teamId),
                    new ApiCallback<TeamStatisticResponse>() {
                        @Override
                        public void onSuccess(TeamStatisticResponse response) {
                            v.displayTeamStatistic(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showLoadingPagingListView(false);
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
