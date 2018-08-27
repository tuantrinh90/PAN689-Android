package com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_teams;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import io.reactivex.disposables.Disposable;

public class DraftTeamsPresenter extends BaseDataPresenter<IDraftTeamsView> implements IDraftTeamsPresenter<IDraftTeamsView> {

    private Disposable getTeam;

    /**
     * @param appComponent
     */
    public DraftTeamsPresenter(AppComponent appComponent) {
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
                    });
            mCompositeDisposable.add(getTeam);
        });
    }
}
