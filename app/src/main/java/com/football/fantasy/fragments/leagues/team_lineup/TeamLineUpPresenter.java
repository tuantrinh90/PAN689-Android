package com.football.fantasy.fragments.leagues.team_lineup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamLineupResponse;
import com.football.models.responses.TeamPitchViewResponse;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeamLineUpPresenter extends BaseDataPresenter<ITeamLineUpView> implements ITeamLineUpPresenter<ITeamLineUpView> {
    /**
     * @param appComponent
     */
    public TeamLineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getPlayers(int teamId) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTeamLineup(teamId),
                    new ApiCallback<TeamLineupResponse>() {
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onComplete() {
                        }

                        @Override
                        public void onSuccess(TeamLineupResponse response) {
                            v.displayMainPlayers(response.getPlayers());
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
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
                        }

                        @Override
                        public void onComplete() {
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
}
