package com.football.fantasy.fragments.leagues.player_pool.display;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PlayerPoolDisplayPresenter extends BaseDataPresenter<IPlayerPoolDisplayView> implements IPlayerPoolDisplayPresenter<IPlayerPoolDisplayView> {
    /**
     * @param appComponent
     */
    public PlayerPoolDisplayPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
