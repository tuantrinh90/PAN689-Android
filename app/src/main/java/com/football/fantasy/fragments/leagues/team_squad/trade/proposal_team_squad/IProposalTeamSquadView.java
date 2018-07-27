package com.football.fantasy.fragments.leagues.team_squad.trade.proposal_team_squad;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface IProposalTeamSquadView extends IBaseMvpView {
    void displayPlayers(List<PlayerResponse> players);
}
