package com.football.fantasy.fragments.leagues.team_squad.trade.proposal;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.listeners.ApiCallback;
import com.football.models.PagingResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.RxUtilities;

public class ProposalDataPresenter extends BaseDataPresenter<IProposalView> implements IProposalPresenter<IProposalView> {
    /**
     * @param appComponent
     */
    protected ProposalDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
