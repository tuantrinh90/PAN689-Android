package com.football.fantasy.fragments.your_team.player_list;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PlayerListPresenter extends BaseDataPresenter<IPlayerListView> implements IPlayerListPresenter<IPlayerListView> {

    /**
     * @param appComponent
     */
    public PlayerListPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
