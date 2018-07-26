package com.football.fantasy.fragments.leagues.team_squad.trade;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TradeRequestResponse;
import com.football.utilities.RxUtilities;

public class TradeDataPresenter extends BaseDataPresenter<ITradeView> implements ITradePresenter<ITradeView> {
    /**
     * @param appComponent
     */
    protected TradeDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTradeRequests(Integer teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getTradeRequests(),
                    new ApiCallback<TradeRequestResponse>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(TradeRequestResponse response) {
                            v.displayTradeRequest(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}