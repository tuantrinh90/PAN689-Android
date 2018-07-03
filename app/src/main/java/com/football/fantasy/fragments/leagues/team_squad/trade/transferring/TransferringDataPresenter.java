package com.football.fantasy.fragments.leagues.team_squad.trade.transferring;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamTransferringResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransferringDataPresenter extends BaseDataPresenter<ITransferringView> implements ITransferringPresenter<ITransferringView> {
    /**
     * @param appComponent
     */
    protected TransferringDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeamTransferring(Integer teamId, String filterPositions, String filterClubs, List<ExtKeyValuePair> displayPairs, int[] sorts) {
        getOptView().doIfPresent(v -> {

            Map<String, String> queries = new HashMap<>();
            queries.put("gameplay_option", "transfer");

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getTeamTransferring(teamId, queries),
                    new ApiCallback<TeamTransferringResponse>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                            v.hideRecyclerViewLoading();
                        }

                        @Override
                        public void onSuccess(TeamTransferringResponse response) {
                            v.displayPlayers(response.getPlayers());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
