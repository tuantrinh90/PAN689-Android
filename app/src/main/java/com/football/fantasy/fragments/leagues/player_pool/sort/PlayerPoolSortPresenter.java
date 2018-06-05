package com.football.fantasy.fragments.leagues.player_pool.sort;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PlayerPoolSortPresenter extends BaseDataPresenter<IPlayerPoolSortView> implements IPlayerPoolSortPresenter<IPlayerPoolSortView> {
    /**
     * @param appComponent
     */
    public PlayerPoolSortPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
