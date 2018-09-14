package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew;

import com.football.common.presenters.IBaseDataPresenter;
import com.football.common.views.IBaseMvpView;

public interface IProposalReviewPresenter<V extends IBaseMvpView> extends IBaseDataPresenter<V> {

    void submitDecision(int requestId, int teamId, int status);

    void cancelDecision(int requestId);
}
