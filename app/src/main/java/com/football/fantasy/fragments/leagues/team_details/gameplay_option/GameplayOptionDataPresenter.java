package com.football.fantasy.fragments.leagues.team_details.gameplay_option;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class GameplayOptionDataPresenter extends BaseDataPresenter<IGameplayOptionView> implements IGameplayOptionPresenter<IGameplayOptionView> {
    /**
     * @param appComponent
     */
    protected GameplayOptionDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}