package com.football.fantasy.fragments.leagues.team_squad.trade.request;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class RequestDataPresenter extends BaseDataPresenter<IRequestView> implements IRequestPresenter<IRequestView> {
    /**
     * @param appComponent
     */
    protected RequestDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
