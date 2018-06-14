package com.football.fantasy.fragments.leagues.your_team.players_popup;

import android.text.TextUtils;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class PlayerPopupDataPresenter extends BaseDataPresenter<IPlayerPopupView> implements IPlayerPopupPresenter<IPlayerPopupView> {
    /**
     * @param appComponent
     */
    protected PlayerPopupDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(int leagueId, String orderBy, int page, int numberPerPage, String query, Integer mainPosition) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put(Constant.KEY_LEAGUE_ID, String.valueOf(leagueId));
            queries.put(Constant.KEY_MAIN_POSITION, String.valueOf(mainPosition));
            queries.put(Constant.KEY_PAGE, String.valueOf(page));
            queries.put(Constant.KEY_PER_PAGE, String.valueOf(numberPerPage));

            if (!TextUtils.isEmpty(orderBy)) {
                queries.put(Constant.KEY_ORDER_BY, orderBy);
            }
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
                            v.notifyDataSetChangedPlayers(response.getData());
                        }

                        @Override
                        public void onError(String error) {
                            v.notifyDataSetChangedPlayers(null);
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
