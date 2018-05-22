package com.football.fantasy.fragments.match_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MatchUpDataPresenter extends BaseDataPresenter<IMatchUpView> implements IMatchUpPresenter<IMatchUpView> {
    /**
     * @param appComponent
     */
    protected MatchUpDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
