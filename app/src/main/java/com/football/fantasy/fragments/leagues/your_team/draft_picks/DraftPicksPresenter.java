package com.football.fantasy.fragments.leagues.your_team.draft_picks;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

import io.reactivex.disposables.Disposable;

public class DraftPicksPresenter extends BaseDataPresenter<IDraftPicksView> implements IDraftPicksPresenter<IDraftPicksView> {

    private Disposable disposableGetPlayers;

    /**
     * @param appComponent
     */
    public DraftPicksPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
