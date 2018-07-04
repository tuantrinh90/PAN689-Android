package com.football.fantasy.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;

import com.bon.util.ActivityUtils;
import com.bon.util.DialogUtils;
import com.football.adapters.PagerAdapter;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.customizes.footers.FooterItem;
import com.football.events.UnauthorizedEvent;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.fragments.home.HomeFragment;
import com.football.fantasy.fragments.leagues.LeagueFragment;
import com.football.fantasy.fragments.match_up.MatchUpFragment;
import com.football.fantasy.fragments.more.MoreFragment;
import com.football.fantasy.fragments.notification.NotificationFragment;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Scanner;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseAppCompatActivity {

    public static final int HOME = 0;
    public static final int LEAGUES = 1;
    public static final int MATCH_UP = 2;
    public static final int NOTIFICATION = 3;
    public static final int MORE = 4;

    @BindView(R.id.aplAppBarLayout)
    AppBarLayout aplAppBarLayout;
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

    @BindView(R.id.viewpager)
    ViewPager viewPager;

    // current tab
    int currentTab = HOME;

    private PagerAdapter mPagerAdapter;

    @Override
    protected int getContentViewId() {
        return R.layout.main_activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSupportActionBar(toolbar);
        initViewPager();
        initFragmentDefault();
        initRxBus();
        if (BuildConfig.DEBUG) {
            test();
        }
    }

    private static final String TAG = "MainActivity";

    void test() {
        Single.create((SingleOnSubscribe<Long>) emitter -> {
            URL url = new URL("https://currentmillis.com/time/minutes-since-unix-epoch.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            InputStream output = connection.getInputStream();
            Scanner s = new Scanner(output).useDelimiter("\\A");
            String response = s.hasNext() ? s.next() : "";
            if (TextUtils.isDigitsOnly(response)) {
                emitter.onSuccess(Long.valueOf(response) * 60 * 1000);
            } else {
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("utc"));
                emitter.onSuccess(cal.getTimeInMillis());
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(time -> {
                    Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("utc"));
                    Log.d(TAG, "test: " + time + " >< " + cal.getTimeInMillis());
                }, throwable -> {
                    throwable.printStackTrace();
                });
    }

    private void initViewPager() {
        mPagerAdapter = new PagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(LeagueFragment.newInstance());
        mPagerAdapter.addFragment(MatchUpFragment.newInstance());
        mPagerAdapter.addFragment(NotificationFragment.newInstance());
        mPagerAdapter.addFragment(MoreFragment.newInstance());
        viewPager.setAdapter(mPagerAdapter);
        viewPager.setOffscreenPageLimit(5);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onClickFooter(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    void initRxBus() {
        mCompositeDisposable.add(bus.ofType(UnauthorizedEvent.class).subscribeWith(new DisposableObserver<UnauthorizedEvent>() {
            @Override
            public void onNext(UnauthorizedEvent unauthorizedEvent) {
                DialogUtils.messageBox(MainActivity.this, getString(R.string.app_name), unauthorizedEvent.getMessage(), getString(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityUtils.startActivity(AccountActivity.class);
                        MainActivity.this.finish();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        }));
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
    public AppBarLayout getAppBarLayout() {
        return aplAppBarLayout;
    }

    @Override
    public Toolbar getToolBar() {
        return toolbar;
    }

    @Override
    public void initFragmentDefault() {
        footerHome.setActiveMode(this, true);
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

    public void onClickFooter(int tabActive) {
        // does not click current tab
        if (tabActive == currentTab) return;
        currentTab = tabActive;

        // clear state
        footerHome.setActiveMode(this, tabActive == HOME);
        footerLeagues.setActiveMode(this, tabActive == LEAGUES);
        footerMatchUp.setActiveMode(this, tabActive == MATCH_UP);
        footerNotification.setActiveMode(this, tabActive == NOTIFICATION);
        footerMore.setActiveMode(this, tabActive == MORE);

        // active tab
        viewPager.setCurrentItem(currentTab);
    }

    public void openOpenLeagueFromLeague() {
        onClickFooter(MainActivity.LEAGUES);
        if (mPagerAdapter.getItem(MainActivity.LEAGUES) instanceof LeagueFragment) {
            ((LeagueFragment) mPagerAdapter.getItem(MainActivity.LEAGUES)).openOpenLeague();
        }
    }
}
