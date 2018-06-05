package com.football.fantasy.fragments.leagues.player_details;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class PlayerDetailPresenter extends BaseDataPresenter<IPlayerDetailView> implements IPlayerDetailPresenter<IPlayerDetailView> {
    /**
     * @param appComponent
     */
    public PlayerDetailPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
