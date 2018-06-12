package com.football.fantasy.fragments.leagues.player_pool;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.ExtPagingResponse;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.Constant;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class PlayerPoolPresenter extends BaseDataPresenter<IPlayerPoolView> implements IPlayerPoolPresenter<IPlayerPoolView> {
    /**
     * @param appComponent
     */
    public PlayerPoolPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers() {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
//            queries.put(Constant.KEY_LEAGUE_ID, String.valueOf(leagueId));
//            queries.put(Constant.KEY_ORDER_BY, orderBy);
//            queries.put(Constant.KEY_PAGE, String.valueOf(page));
//            queries.put(Constant.KEY_PER_PAGE, String.valueOf(perPage));
//            queries.put(Constant.KEY_WORD, query);
//            queries.put(Constant.KEY_MAIN_POSITION, String.valueOf(mainPosition));

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
