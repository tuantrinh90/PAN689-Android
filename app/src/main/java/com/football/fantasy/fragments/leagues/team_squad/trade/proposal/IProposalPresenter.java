package com.football.fantasy.fragments.leagues.team_squad.trade.proposal;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IProposalPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {
    void makeProposal(int teamId, int withTeamId, int[] fromPlayerId, int[] toPlayerId);
}
