package com.football.fantasy.fragments.leagues.pending_invitation;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.LeagueResponse;

import java.util.List;

public interface IPendingInvitationView extends IBaseMvpView {
    void notifyDataSetChanged(List<LeagueResponse> its);

    void removeItem(LeagueResponse leagueResponse);

    void goCreateTeam(LeagueResponse league);
}
