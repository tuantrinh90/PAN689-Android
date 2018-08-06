package com.football.fantasy.fragments.leagues.team_details.gameplay_option.proposal_team_squad;

import com.football.common.views.IBaseMvpView;
import com.football.models.responses.PlayerResponse;

import java.util.List;

public interface IProposalTeamSquadView extends IBaseMvpView {
    void displayPlayers(List<PlayerResponse> players);
}
