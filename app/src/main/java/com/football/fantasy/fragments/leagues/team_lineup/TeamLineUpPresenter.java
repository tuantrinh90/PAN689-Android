package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamPitchViewResponse;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;

public class TeamLineUpPresenter extends BaseDataPresenter<ITeamLineUpView> implements ITeamLineUpPresenter<ITeamLineUpView> {
    /**
     * @param appComponent
     */
    public TeamLineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPitchView(Integer teamId, String value) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            // sort
            JSONArray filter = new JSONArray();
            try {
                JSONObject filterObj = new JSONObject();
                filterObj.put("property", "formation");
                filterObj.put("rela", "team_round");
                filterObj.put("operator", "eq");
                filterObj.put("value", value);
                filter.put(filterObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            queries.put("filter", filter.toString());

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getPitchView(teamId, queries),
                    new ApiCallback<TeamPitchViewResponse>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(TeamPitchViewResponse response) {
                            v.displayMainPlayers(response.getMainPlayers());
                            v.displayMinorPlayers(response.getMinorPlayers());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void addPlayerToPitchView(Integer teamId, PlayerResponse player, Integer position, Integer order) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            queries.put("pick_order", String.valueOf(order));

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updatePitchView(
                            teamId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("player_id", String.valueOf(player.getId()))
                                    .build(),
                            queries),
                    new ApiCallback<Object>() {
                        @Override
                        public void onStart() {
                            v.showLoading(true);
                        }

                        @Override
                        public void onComplete() {
                            v.showLoading(false);
                        }

                        @Override
                        public void onSuccess(Object response) {
                            v.onAddPlayer(player, position, order);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
