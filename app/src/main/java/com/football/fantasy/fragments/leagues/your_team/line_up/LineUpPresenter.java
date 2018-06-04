package com.football.fantasy.fragments.leagues.your_team.line_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class LineUpPresenter extends BaseDataPresenter<ILineUpView> implements ILineUpPresenter<ILineUpView> {

    /**
     * @param appComponent
     */
    public LineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
