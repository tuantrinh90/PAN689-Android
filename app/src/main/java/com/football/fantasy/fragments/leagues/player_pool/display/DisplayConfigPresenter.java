package com.football.fantasy.fragments.leagues.player_pool.display;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class DisplayConfigPresenter extends BaseDataPresenter<IDisplayConfigView> implements IDisplayConfigPresenter<IDisplayConfigView> {
    /**
     * @param appComponent
     */
    public DisplayConfigPresenter(AppComponent appComponent) {
        super(appComponent);
    }


}
