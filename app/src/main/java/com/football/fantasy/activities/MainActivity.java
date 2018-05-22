package com.football.fantasy.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;

import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.footers.FooterItem;
import com.football.fantasy.R;
import com.football.fantasy.fragments.home.HomeFragment;
import com.football.fantasy.fragments.leagues.LeagueFragment;
import com.football.fantasy.fragments.match_up.MatchUpFragment;
import com.football.fantasy.fragments.more.MoreFragment;
import com.football.fantasy.fragments.notification.NotificationFragment;
import com.football.utilities.FragmentUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseAppCompatActivity {
    static final String TAG = MainActivity.class.getSimpleName();

    static final int HOME = 0;
    static final int LEAGUES = 1;
    static final int MATCH_UP = 2;
    static final int NOTIFICATION = 3;
    static final int MORE = 4;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.footerHome)
    FooterItem footerHome;
    @BindView(R.id.footerLeagues)
    FooterItem footerLeagues;
    @BindView(R.id.footerMatchUp)
    FooterItem footerMatchUp;
    @BindView(R.id.footerNotification)
    FooterItem footerNotification;
    @BindView(R.id.footerMore)
    FooterItem footerMore;

    // current tab
    int currentTab = HOME;
    BaseMvpFragment fragment = null;

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        initFragmentDefault();
    }

    @Override
    public void onBackPressed() {
        onBackPressedAction();
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return getSupportActionBar();
    }

    @Override
    public Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void initFragmentDefault() {
        onClickFooter(currentTab);
    }

    @OnClick(R.id.footerHome)
    void onClickFooterHome() {
        onClickFooter(HOME);
    }

    @OnClick(R.id.footerLeagues)
    void onClickFooterLeagues() {
        onClickFooter(LEAGUES);
    }

    @OnClick(R.id.footerMatchUp)
    void onClickFooterMatchUp() {
        onClickFooter(MATCH_UP);
    }

    @OnClick(R.id.footerNotification)
    void onClickFooterNotification() {
        onClickFooter(NOTIFICATION);
    }

    @OnClick(R.id.footerMore)
    void onClickFooterMore() {
        onClickFooter(MORE);
    }

    void onClickFooter(int tabActive) {
        // does not click current tab
        if (fragment != null) {
            if (tabActive == HOME && fragment instanceof HomeFragment) return;
            if (tabActive == LEAGUES && fragment instanceof LeagueFragment) return;
            if (tabActive == MATCH_UP && fragment instanceof MatchUpFragment) return;
            if (tabActive == NOTIFICATION && fragment instanceof NotificationFragment) return;
            if (tabActive == MORE && fragment instanceof MoreFragment) return;
        }

        // clear state
        footerHome.setActiveMode(this, tabActive == HOME);
        footerLeagues.setActiveMode(this, tabActive == LEAGUES);
        footerMatchUp.setActiveMode(this, tabActive == MATCH_UP);
        footerNotification.setActiveMode(this, tabActive == NOTIFICATION);
        footerMore.setActiveMode(this, tabActive == MORE);

        // active tab
        switch (tabActive) {
            case HOME:
                fragment = HomeFragment.newInstance();
                break;
            case LEAGUES:
                fragment = LeagueFragment.newInstance();
                break;
            case MATCH_UP:
                fragment = MatchUpFragment.newInstance();
                break;
            case NOTIFICATION:
                fragment = NotificationFragment.newInstance();
                break;
            case MORE:
                fragment = MoreFragment.newInstance();
                break;
        }

        // current tab
        FragmentUtils.replaceFragment(this, fragment);
        fragments.clear();
    }
}
