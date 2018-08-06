package com.football.fantasy.fragments.leagues.team_details.gameplay_option.proposal_team_squad;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IProposalTeamSquadPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void getLineup(Integer leagueId, String property, String direction);
}
