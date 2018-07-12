package com.football.fantasy.fragments.leagues.player_pool;

import android.text.TextUtils;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.listview.ExtPagingListView;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.PagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.SeasonResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerPoolPresenter extends BaseDataPresenter<IPlayerPoolView> implements IPlayerPoolPresenter<IPlayerPoolView> {
    /**
     * @param appComponent
     */
    public PlayerPoolPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(String seasonId, String positions, String clubs, List<ExtKeyValuePair> displayPairs, int[] sorts, int page, String query) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put(Constant.KEY_SEASON, seasonId);
            queries.put(Constant.KEY_PAGE, String.valueOf(page));
            queries.put(Constant.KEY_PER_PAGE, String.valueOf(ExtPagingListView.NUMBER_PER_PAGE));

            if (!TextUtils.isEmpty(query)) {
                queries.put(Constant.KEY_WORD, query);
            }

            if (!TextUtils.isEmpty(positions)) {
                queries.put(Constant.KEY_MAIN_POSITION, positions);
            }

            if (!TextUtils.isEmpty(clubs)) {
                queries.put(Constant.KEY_CLUBS, clubs);
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
                            v.displayPlayers(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void getSeasons() {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getSeasons(),
                    new ApiCallback<PagingResponse<SeasonResponse>>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(PagingResponse<SeasonResponse> response) {
                            v.displaySeasons(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
