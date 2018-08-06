package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.choose_a_team;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

public class ChooseATeamDataPresenter extends BaseDataPresenter<IChooseATeamView> implements IChooseATeamPresenter<IChooseATeamView> {
    /**
     * @param appComponent
     */
    protected ChooseATeamDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeams(int leagueId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTeams(leagueId),
                    new ApiCallback<PagingResponse<TeamResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<TeamResponse> response) {
                            v.displayTeams(response.getData());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
