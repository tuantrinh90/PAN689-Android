package com.football.fantasy.fragments.leagues.league_details.successor;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class SuccessorPresenter extends BaseDataPresenter<ISuccessorView> implements ISuccessorPresenter<ISuccessorView> {
    /**
     * @param appComponent
     */
    public SuccessorPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
