package com.football.fantasy.fragments.more;

import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MoreDataPresenter extends BaseDataPresenter<IMoreView> implements IMorePresenter<IMoreView> {
    /**
     * @param appComponent
     */
    protected MoreDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }
}
