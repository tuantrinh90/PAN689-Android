package com.football.fantasy.fragments.leagues.team_squad.trade.record;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class RecordDataPresenter extends BaseDataPresenter<IRecordView> implements IRecordPresenter<IRecordView> {
    /**
     * @param appComponent
     */
    protected RecordDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
