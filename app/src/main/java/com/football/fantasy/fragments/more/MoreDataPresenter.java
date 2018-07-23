package com.football.fantasy.fragments.more;

import com.bon.share_preferences.AppPreferences;
import com.football.common.presenters.BaseDataPresenter;
import com.football.di.AppComponent;
import com.football.helpers.sociallogin.facebook.FacebookHelper;
import com.football.helpers.sociallogin.google.GoogleHelper;
import com.football.utilities.Constant;
import com.football.utilities.ServiceConfig;

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
            String provider = AppPreferences.getInstance(v.getAppActivity().getAppContext()).getString(Constant.KEY_LOGIN_TYPE);
            switch (provider) {
                case ServiceConfig.PROVIDER_FACEBOOK:
                    FacebookHelper.quickSignOut();
                    break;

                case ServiceConfig.PROVIDER_GOOGLE:
                    GoogleHelper.quickSignOut(v.getAppActivity());
                    break;

                case ServiceConfig.PROVIDER_TWITTER:
                default:
                    break;
            }
            handleLogout(v);
        });
    }

    private void handleLogout(IMoreView v) {
        AppPreferences.getInstance(v.getAppActivity()).clearCache();
        v.logout();
    }
}
