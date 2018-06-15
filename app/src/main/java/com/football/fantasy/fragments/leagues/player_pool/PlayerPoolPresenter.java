package com.football.fantasy.fragments.leagues.player_pool;

import android.text.TextUtils;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

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
    public void getPlayers(String positions, String clubs, List<ExtKeyValuePair> displayPairs, boolean[] sorts) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            if (!TextUtils.isEmpty(positions)) {
                queries.put(Constant.KEY_MAIN_POSITION, positions);
            }
            if (!TextUtils.isEmpty(clubs)) {
                queries.put(Constant.KEY_CLUBS, clubs);
            }
            JSONObject sort = new JSONObject();
            for (int i = 0, size = displayPairs.size(); i < size; i++) {
                ExtKeyValuePair pair = displayPairs.get(i);
                try {
                    sort.put(pair.getKey(), sorts[i] ? "desc" : "asc");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                queries.put(Constant.KEY_ORDER_BY, sort.toString());
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
}
