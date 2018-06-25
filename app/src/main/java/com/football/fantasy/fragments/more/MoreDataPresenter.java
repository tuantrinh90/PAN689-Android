package com.football.fantasy.fragments.more;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;

public class MoreDataPresenter extends BaseDataPresenter<IMoreView> implements IMorePresenter<IMoreView> {
    /**
     * @param appComponent
     */
    protected MoreDataPresenter(AppComponent appComponent) {
        super(appComponent);
    }

    @Override
    public void logout() {
        getOptView().doIfPresent(v -> {
            AppPreferences.getInstance(v.getAppActivity()).clearCache();
            v.logout();
        });
    }
}
