package com.football.fantasy;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.footers.FooterItem;
import com.football.fantasy.fragments.home.HomeFragment;
import com.football.fantasy.fragments.leagues.LeagueFragment;
import com.football.fantasy.fragments.match_up.MatchUpFragment;
import com.football.fantasy.fragments.more.MoreFragment;
import com.football.fantasy.fragments.notification.NotificationFragment;
import com.football.utilities.FragmentUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseAppCompatActivity {
    enum TabActive {
        HOME,
        LEAGUES,
        MATCH_UP,
        NOTIFICATION,
        MORE
    }

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
    TabActive currentTab = TabActive.HOME;

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        onClickFooter(currentTab);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return getSupportActionBar();
    }

    @Override
    public Toolbar getToolBar() {
        return toolbar;
    }

    void onClickFooter(TabActive tabActive) {
        BaseMvpFragment fragment = null;

        // clear state
        footerHome.setActiveMode(this, false);
        footerLeagues.setActiveMode(this, false);
        footerMatchUp.setActiveMode(this, false);
        footerNotification.setActiveMode(this, false);
        footerMore.setActiveMode(this, false);

        // active tab
        switch (tabActive) {
            case HOME:
                footerHome.setActiveMode(this, true);
                fragment = HomeFragment.newInstance();
                break;
            case LEAGUES:
                footerLeagues.setActiveMode(this, true);
                fragment = LeagueFragment.newInstance();
                break;
            case MATCH_UP:
                footerMatchUp.setActiveMode(this, true);
                fragment = MatchUpFragment.newInstance();
                break;
            case NOTIFICATION:
                footerNotification.setActiveMode(this, true);
                fragment = NotificationFragment.newInstance();
                break;
            case MORE:
                footerMore.setActiveMode(this, true);
                fragment = MoreFragment.newInstance();
                break;
        }

        // current tab
        FragmentUtils.replaceFragment(this, fragment);
        currentTab = tabActive;
        fragments.clear();
    }

    @OnClick(R.id.footerHome)
    void onClickFooterHome() {
        onClickFooter(TabActive.HOME);
    }

    @OnClick(R.id.footerLeagues)
    void onClickFooterLeagues() {
        onClickFooter(TabActive.LEAGUES);
    }

    @OnClick(R.id.footerMatchUp)
    void onClickFooterMatchUp() {
        onClickFooter(TabActive.MATCH_UP);
    }

    @OnClick(R.id.footerNotification)
    void onClickFooterNotification() {
        onClickFooter(TabActive.NOTIFICATION);
    }

    @OnClick(R.id.footerMore)
    void onClickFooterMore() {
        onClickFooter(TabActive.MORE);
    }
}
