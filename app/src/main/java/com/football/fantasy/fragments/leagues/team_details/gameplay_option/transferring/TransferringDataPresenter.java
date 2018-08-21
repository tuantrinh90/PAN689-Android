package com.football.fantasy.fragments.leagues.team_details.gameplay_option.transferring;

import android.text.TextUtils;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamTransferringResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.disposables.Disposable;
import okhttp3.MultipartBody;

public class TransferringDataPresenter extends BaseDataPresenter<ITransferringView> implements ITransferringPresenter<ITransferringView> {
    private Disposable disposableGetPlayers;

    /**
     * @param appComponent
     */
    protected TransferringDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeamTransferring(Integer teamId, String gameplay, String filterPositions, String filterClubs, List<ExtKeyValuePair> displayPairs, int[] sorts) {
        getOptView().doIfPresent(v -> {

            Map<String, String> queries = new HashMap<>();
            queries.put("gameplay_option", gameplay);

            if (!TextUtils.isEmpty(filterPositions)) {
                queries.put(Constant.KEY_MAIN_POSITION, filterPositions);
            }

            if (!TextUtils.isEmpty(filterClubs)) {
                queries.put(Constant.KEY_CLUBS, filterClubs);
            }

            JSONArray sort = new JSONArray();
            for (int i = 0, size = displayPairs.size(); i < size; i++) {
                JSONObject sortObj = new JSONObject();
                ExtKeyValuePair pair = displayPairs.get(i);
                try {
                    if (sorts[i] != Constant.SORT_NONE) {
                        sortObj.put("property", pair.getKey());
                        sortObj.put("direction", sorts[i] == Constant.SORT_DESC ? "desc" : "asc");
                        sort.put(sortObj);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (sort.length() > 0) {
                    queries.put(Constant.KEY_SORT, sort.toString());
                }
            }

            if (disposableGetPlayers != null) {
                mCompositeDisposable.remove(disposableGetPlayers);
            }
            disposableGetPlayers = RxUtilities.async(
                    v,
                    dataModule.getApiService().getTeamTransferring(teamId, queries),
                    new ApiCallback<TeamTransferringResponse>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(TeamTransferringResponse response) {
                            v.displayPlayers(response.getPlayers());
                            v.displayInjuredPlayers(response.getInjuredPlayers());
                            v.displayHeader(
                                    response.getCurrentTransferPlayer() < response.getMaxTransferPlayer(),
                                    response.getTransferPlayerLeftDisplay(),
                                    response.getTransferTimeLeft(),
                                    response.getTeam().getCurrentBudget());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    });
            mCompositeDisposable.add(disposableGetPlayers);
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
