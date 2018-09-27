package com.football.fantasy.fragments.leagues.player_pool.sort;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class SortPresenter extends BaseDataPresenter<ISortView> implements ISortPresenter<ISortView> {
    /**
     * @param appComponent
     */
    public SortPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
