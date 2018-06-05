package com.football.fantasy.fragments.leagues.player_pool;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PlayerPoolPresenter extends BaseDataPresenter<IPlayerPoolView> implements IPlayerPoolPresenter<IPlayerPoolView> {
    /**
     * @param appComponent
     */
    public PlayerPoolPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
