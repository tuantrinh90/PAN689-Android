package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TradeResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class ProposalDataReviewPresenter extends BaseDataPresenter<IProposalReviewView> implements IProposalReviewPresenter<IProposalReviewView> {
    /**
     * @param appComponent
     */
    protected ProposalDataReviewPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void submitDecision(int requestId, int teamId, int status) {
        getOptView().doIfPresent(v -> {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("status", String.valueOf(status));
//                    .addFormDataPart("team_id", String.valueOf(teamId));

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().submitTradeDecision(requestId, builder.build()),
                    new ApiCallback<TradeResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(TradeResponse response) {
                            v.submitSuccess(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showLoading(false);
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void cancelDecision(int requestId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().cancelTradeDecision(requestId),
                    new ApiCallback<TradeResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(TradeResponse response) {
                            v.submitSuccess(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showLoading(false);
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
