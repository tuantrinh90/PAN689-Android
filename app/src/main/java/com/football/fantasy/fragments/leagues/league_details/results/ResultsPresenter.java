package com.football.fantasy.fragments.leagues.league_details.results;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class ResultsPresenter extends BaseDataPresenter<IResultsView> implements IResultsPresenter<IResultsView> {
    /**
     * @param appComponent
     */
    public ResultsPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
