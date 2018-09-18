package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

public class TradeDataPresenter extends BaseDataPresenter<ITradeView> implements ITradePresenter<ITradeView> {
    /**
     * @param appComponent
     */
    protected TradeDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeamDetail(int teamId) {
        getOptView().doIfPresent(v -> {

            mCompositeDisposable.add(RxUtilities.async(
                    v,
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
                            v.displayTeam(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showLoading(false);
                            v.showMessage(error);
                        }
                    }));
        });
    }
}