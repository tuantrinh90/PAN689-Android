package com.football.fantasy.fragments.more;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MorePresenter extends BaseDataPresenter<IMoreView> implements IMorePresenter<IMoreView> {
    /**
     * @param appComponent
     */
    protected MorePresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
