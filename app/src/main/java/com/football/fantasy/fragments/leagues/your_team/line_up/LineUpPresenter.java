package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public abstract class LineUpPresenter<V extends ILineUpView> extends BaseDataPresenter<V> implements ILineUpPresenter<V> {

    /**
     * @param appComponent
     */
    public LineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }

}
