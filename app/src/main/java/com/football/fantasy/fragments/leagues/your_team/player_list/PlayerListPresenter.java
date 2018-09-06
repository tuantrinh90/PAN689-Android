package com.football.fantasy.fragments.leagues.your_team.player_list;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public abstract class PlayerListPresenter<V extends IPlayerListView> extends BaseDataPresenter<V> implements IPlayerListPresenter<V> {

    /**
     * @param appComponent
     */
    public PlayerListPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
