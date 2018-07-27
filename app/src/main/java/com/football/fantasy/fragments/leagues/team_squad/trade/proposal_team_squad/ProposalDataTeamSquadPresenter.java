package com.football.fantasy.fragments.leagues.team_squad.trade.proposal_team_squad;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.responses.LineupResponse;
import com.football.utilities.RxUtilities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProposalDataTeamSquadPresenter extends BaseDataPresenter<IProposalTeamSquadView> implements IProposalTeamSquadPresenter<IProposalTeamSquadView> {
    /**
     * @param appComponent
     */
    protected ProposalDataTeamSquadPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void getLineup(Integer teamId, String property, String direction) {
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
            mCompositeDisposable.add(RxUtilities.async(
                    v,
                    dataModule.getApiService().getLineup(teamId), // todo: truyền sai tham số
                    new ApiCallback<LineupResponse>() {
                        @Override
                        public void onSuccess(LineupResponse response) {
                            v.displayPlayers(response.getPlayers());
                        }

                        @Override
                        public void onError(String error) {
                            v.showMessage(error);
                        }
                    }));
        });
    }
}
