package com.football.fantasy.fragments.leagues.player_pool.filter;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PlayerPoolFilterPresenter extends BaseDataPresenter<IPlayerPoolFilterView> implements IPlayerPoolFilterPresenter<IPlayerPoolFilterView> {
    /**
     * @param appComponent
     */
    public PlayerPoolFilterPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
