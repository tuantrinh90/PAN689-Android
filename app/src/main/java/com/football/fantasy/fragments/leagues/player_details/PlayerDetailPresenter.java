package com.football.fantasy.fragments.leagues.player_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.PlayerStatisticResponse;
import com.football.utilities.RxUtilities;

import java.util.HashMap;
import java.util.Map;

public class PlayerDetailPresenter extends BaseDataPresenter<IPlayerDetailView> implements IPlayerDetailPresenter<IPlayerDetailView> {
    /**
     * @param appComponent
     */
    public PlayerDetailPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayerStatistic(Integer playerId, String filter) {
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
                    dataModule.getApiService().getPlayerStatistic(playerId, filter),
                    new ApiCallback<PlayerStatisticResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(PlayerStatisticResponse response) {
                            v.displayPoints(response.getTotalPoint());

                            if (response.getMeta() != null) {
                                v.displayStatistic(response.getMeta());

                            } else {
                                v.displayStatistics(response.getMetas());
                            }
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
