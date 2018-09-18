package com.football.fantasy.fragments.leagues.player_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.PlayerStatisticResponse;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    public void getPlayerDetail(int playerId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getPlayerDetail(playerId),
                    new ApiCallback<PlayerResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(PlayerResponse response) {
                           v.displayPlayer(response);
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }

    @Override
    public void getPlayerStatistic(int playerId, int teamId, String property, String value) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            JSONArray filter = new JSONArray();
            JSONObject object = new JSONObject();
            try {
                object.put("property", property);
                object.put("operator", "eq");
                object.put("value", value);

                filter.put(object);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            queries.put("filter", filter.toString());

            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    teamId > 0 ? dataModule.getApiService().getPlayerStatisticWithTeam(playerId, teamId, queries)
                            : dataModule.getApiService().getPlayerStatistic(playerId, queries),
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
