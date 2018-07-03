package com.football.fantasy.fragments.leagues.team_squad;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.TeamSquadResponse;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TeamSquadPresenter extends BaseDataPresenter<ITeamSquadView> implements ITeamSquadPresenter<ITeamSquadView> {
    /**
     * @param appComponent
     */
    public TeamSquadPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getTeamSquad(Integer teamId, String property, String direction) {
        getOptView().doIfPresent(v -> {
            Map<String, String> queries = new HashMap<>();
            JSONArray sortArray = new JSONArray();

            try {
                JSONObject p = new JSONObject();
                p.put("property", property);
                p.put("direction", direction);
                sortArray.put(p);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (sortArray.length() > 0) {
                queries.put("sort", sortArray.toString());
            }
            mCompositeDisposable.add(RxUtilities.async(v,
                    dataModule.getApiService().getTeamSquad(teamId, queries),
                    new ApiCallback<TeamSquadResponse>() {

                        @Override
                        public void onSuccess(TeamSquadResponse response) {
                            v.displayTeamSquad(response);
                        }

                        @Override
                        public void onError(String e) {
                            v.showMessage(e);
                        }
                    }));
        });
    }
}
