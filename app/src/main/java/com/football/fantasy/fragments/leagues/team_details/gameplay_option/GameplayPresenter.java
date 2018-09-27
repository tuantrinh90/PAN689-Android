package com.football.fantasy.fragments.leagues.team_details.gameplay_option;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class GameplayPresenter extends BaseDataPresenter<IGameplayView> implements IGameplayPresenter<IGameplayView> {
    /**
     * @param appComponent
     */
    protected GameplayPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}