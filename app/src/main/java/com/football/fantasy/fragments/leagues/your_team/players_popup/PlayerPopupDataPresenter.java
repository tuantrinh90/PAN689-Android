package com.football.fantasy.fragments.leagues.your_team.players_popup;

import android.text.TextUtils;

import com.bon.customview.listview.ExtPagingListView;
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

import io.reactivex.disposables.Disposable;

public class PlayerPopupDataPresenter extends BaseDataPresenter<IPlayerPopupView> implements IPlayerPopupPresenter<IPlayerPopupView> {
    private Disposable disposableGetPlayers;

    /**
     * @param appComponent
     */
    protected PlayerPopupDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(int leagueId, int valueDirection, int page, String query, Integer mainPosition, String clubs) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put(Constant.KEY_LEAGUE_ID, String.valueOf(leagueId));
            queries.put(Constant.KEY_MAIN_POSITION, String.valueOf(mainPosition));
            queries.put(Constant.KEY_PAGE, String.valueOf(page));
            queries.put(Constant.KEY_PER_PAGE, String.valueOf(ExtPagingListView.NUMBER_PER_PAGE));

            if (!TextUtils.isEmpty(clubs)) {
                queries.put(Constant.KEY_CLUBS, clubs);
            }

            if (valueDirection != Constant.SORT_NONE) {
                JSONArray sorts = new JSONArray();
                try {
                    JSONObject value = new JSONObject();
                    value.put("property", "value");
                    value.put("direction", valueDirection == Constant.SORT_ASC ? "asc" : "desc");
                    sorts.put(value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                queries.put(Constant.KEY_SORT, sorts.toString());
            }
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
