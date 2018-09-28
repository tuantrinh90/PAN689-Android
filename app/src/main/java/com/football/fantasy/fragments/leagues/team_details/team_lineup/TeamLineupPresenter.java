package com.football.fantasy.fragments.leagues.team_details.team_lineup;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TeamPitchViewResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.MultipartBody;

public class TeamLineupPresenter extends BaseDataPresenter<ITeamLineupView> implements ITeamLineupPresenter<ITeamLineupView> {
    /**
     * @param appComponent
     */
    public TeamLineupPresenter(AppComponent appComponent) {
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
                filterObj.put("property", "total_point");
                filterObj.put("direction", "asc");
                filter.put(filterObj);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            queries.put("sort", filter.toString());

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
                            showResponse(v, response);
                            if (response.getTeam() != null) {
                                v.displayTeam(response.getTeam());
                            }
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void addPlayerToPitchView(Integer teamId, int round, PlayerResponse fromPlayer, PlayerResponse toPlayer, int position, int order) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();

            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().updatePitchView(
                            teamId,
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("round", String.valueOf(round))
                                    .addFormDataPart("from_player_id", String.valueOf(fromPlayer == null ? 0 : fromPlayer.getId()))
                                    .addFormDataPart("to_player_id", String.valueOf(toPlayer.getId()))
                                    .addFormDataPart("position", String.valueOf(position))
                                    .addFormDataPart("order", String.valueOf(order))
                                    .build(),
                            queries),
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
                            showResponse(v, response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    @Override
    public void changeTeamFormation(TeamResponse team, String formationValue) {
        getOptView().doIfPresent(v -> {
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().changeTeamFormation(
                            team.getId(),
                            new MultipartBody.Builder()
                                    .setType(MultipartBody.FORM)
                                    .addFormDataPart("round", String.valueOf(team.getRound()))
                                    .addFormDataPart("formation", formationValue)
                                    .build()),
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
                            showResponse(v, response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }

    private void showResponse(ITeamLineupView v, TeamPitchViewResponse response) {
        if (response != null) {
            if (response.getPlayers() != null) {
                v.displayMainPlayers(response.getPlayers());
            }
            if (response.getMorePlayers() != null) {
                v.displayMinorPlayers(response.getMorePlayers());
            }
        }
    }
}
