package com.football.fantasy.fragments.leagues.your_team.player_list.transfer;

import android.text.TextUtils;

import com.bon.customview.listview.ExtPagingListView;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListPresenter;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.disposables.Disposable;

public class PlayerListTransferPresenter extends PlayerListPresenter<IPlayerListTransferView> implements IPlayerListTransferPresenter<IPlayerListTransferView> {

    private Disposable disposableGetPlayers;

    /**
     * @param appComponent
     */
    public PlayerListTransferPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(int leagueId, boolean valueSortDesc, int page, String query, String filterPositions, String filterClubs) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put(Constant.KEY_LEAGUE_ID, String.valueOf(leagueId));
            queries.put(Constant.KEY_PAGE, String.valueOf(page));
            queries.put(Constant.KEY_PER_PAGE, String.valueOf(ExtPagingListView.NUMBER_PER_PAGE));

            if (!TextUtils.isEmpty(filterPositions)) {
                queries.put(Constant.KEY_MAIN_POSITION, String.valueOf(filterPositions));
            }

            if (!TextUtils.isEmpty(filterClubs)) {
                queries.put(Constant.KEY_CLUBS, String.valueOf(filterClubs));
            }

            // sort
            JSONArray sort = new JSONArray();
            try {
                JSONObject transfer = new JSONObject();
                transfer.put("property", PlayerResponse.Options.VALUE);
                transfer.put("direction", valueSortDesc ? "desc" : "asc");
                sort.put(transfer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            queries.put(Constant.KEY_SORT, sort.toString());

            if (!TextUtils.isEmpty(query)) {
                queries.put(Constant.KEY_WORD, query);
            }

            if (disposableGetPlayers != null) {
                mCompositeDisposable.remove(disposableGetPlayers);
            }

            disposableGetPlayers = RxUtilities.async(
                    v,
                    dataModule.getApiService().getPlayerList(queries),
                    new ApiCallback<ExtPagingResponse<PlayerResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoadingPagingListView(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoadingPagingListView(false);
                        }

                        @Override
                        public void onSuccess(ExtPagingResponse<PlayerResponse> response) {
                            v.displayPlayers(response.getData());
                            v.displayStatistic(response.getStatistic());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    });
            mCompositeDisposable.add(disposableGetPlayers);
        });
    }

}
