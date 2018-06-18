package com.football.fantasy.fragments.leagues.your_team.player_list;

import android.text.TextUtils;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
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

public class PlayerListPresenter extends BaseDataPresenter<IPlayerListView> implements IPlayerListPresenter<IPlayerListView> {

    /**
     * @param appComponent
     */
    public PlayerListPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(int leagueId, boolean valueSortDesc, int page, int perPage, String query, String filterPositions, String filterClubs, boolean newPlayers) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put(Constant.KEY_LEAGUE_ID, String.valueOf(leagueId));
            queries.put(Constant.KEY_PAGE, String.valueOf(page));
            queries.put(Constant.KEY_PER_PAGE, String.valueOf(perPage));

            if (!TextUtils.isEmpty(filterPositions)) {
                queries.put(Constant.KEY_MAIN_POSITION, String.valueOf(filterPositions));
            }

            if (!TextUtils.isEmpty(filterClubs)) {
                queries.put(Constant.KEY_CLUBS, String.valueOf(filterPositions));
            }

            // sort
            JSONArray sort = new JSONArray();
            try {
                JSONObject transfer = new JSONObject();
                transfer.put("property", "transfer_value");
                transfer.put("direction", valueSortDesc ? "desc" : "asc");
                sort.put(transfer);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            queries.put(Constant.KEY_ORDER_BY, sort.toString());

            if (!TextUtils.isEmpty(query)) {
                queries.put(Constant.KEY_WORD, query);
            }


            mCompositeDisposable.add(RxUtilities.async(
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
                            v.notifyDataSetChangedPlayers(response.getData(), newPlayers);
                            v.displayStatistic(response.getStatistic());
                        }

                        @Override
                        public void onError(String error) {
                            v.notifyDataSetChangedPlayers(null, newPlayers);
                            v.showMessage(error);
                        }
                    }));
        });
    }

}
