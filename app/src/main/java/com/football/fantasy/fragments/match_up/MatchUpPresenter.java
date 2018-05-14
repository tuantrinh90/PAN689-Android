package com.football.fantasy.fragments.match_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MatchUpPresenter extends BaseDataPresenter<IMatchUpView> implements IMatchUpPresenter<IMatchUpView> {
    /**
     * @param appComponent
     */
    protected MatchUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
