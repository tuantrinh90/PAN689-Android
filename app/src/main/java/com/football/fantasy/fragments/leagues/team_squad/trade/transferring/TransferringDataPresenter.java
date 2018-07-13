package com.football.fantasy.fragments.leagues.team_squad.trade.transferring;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamTransferringResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;

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
                        }

                        @Override
                        public void onSuccess(TeamTransferringResponse response) {
                            v.displayPlayers(response.getPlayers());
                            v.displayInjuredPlayers(response.getInjuredPlayers());
                            v.displayHeader(response.getTransferPlayerLeftDisplay(), response.getTransferTimeLeft(), response.getTeam().getCurrentBudget());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void transferPlayer(Integer teamId, String gameplayOption, PlayerResponse fromPlayer, PlayerResponse toPlayer) {
        getOptView().doIfPresent(v -> {

            MultipartBody.Builder builder = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("gameplay_option", gameplayOption)
                    .addFormDataPart("from_player_id", String.valueOf(fromPlayer.getId()))
                    .addFormDataPart("to_player_id", String.valueOf(toPlayer.getId()));

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().transferPlayer(teamId, builder.build()),
                    new ApiCallback<Object>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(Object response) {
                            v.transferSuccess();
                        }

                        @Override
                        public void onError(String error) {
                            v.transferError(error);
                        }
                    }));
        });
    }
}
