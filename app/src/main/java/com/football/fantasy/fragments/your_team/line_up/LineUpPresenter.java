package com.football.fantasy.fragments.your_team.line_up;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.fantasy.fragments.your_team.IYourTeamPresenter;
import com.football.fantasy.fragments.your_team.IYourTeamView;

public class LineUpPresenter extends BaseDataPresenter<ILineUpView> implements ILineUpPresenter<ILineUpView> {

    /**
     * @param appComponent
     */
    public LineUpPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
