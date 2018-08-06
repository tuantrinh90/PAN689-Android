package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TradeResponse;
import com.football.utilities.RxUtilities;

import okhttp3.MultipartBody;

public class ProposalDataPresenter extends BaseDataPresenter<IProposalView> implements IProposalPresenter<IProposalView> {
    /**
     * @param appComponent
     */
    protected ProposalDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void makeProposal(int teamId, int withTeamId, int[] fromPlayerId, int[] toPlayerId) {
        getOptView().doIfPresent(v -> {
            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("team_id", String.valueOf(teamId))
                    .addFormDataPart("with_team_id", String.valueOf(withTeamId));

            for (int index = 0; index < 3; index++) {
                if (fromPlayerId[index] != 0) {
                    builder.addFormDataPart(String.format("from_player_id[%d]", index), String.valueOf(fromPlayerId[index]));
                }
                if (toPlayerId[index] != 0) {
                    builder.addFormDataPart(String.format("to_player_id[%d]", index), String.valueOf(toPlayerId[index]));
                }
            }

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().submitTradeRequests(builder.build()),
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
