package com.football.fantasy.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.bon.share_preferences.AppPreferences;
import com.bon.util.ActivityUtils;
import com.bon.util.DialogUtils;
import com.bon.util.StringUtils;
import com.football.adapters.StatePagerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.customizes.footers.FooterItem;
import com.football.events.UnauthorizedEvent;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.fragments.home.HomeFragment;
import com.football.fantasy.fragments.leagues.LeagueFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.PlayerPoolFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.TeamSquadFragment;
import com.football.fantasy.fragments.match_up.MatchUpFragment;
import com.football.fantasy.fragments.more.MoreFragment;
import com.football.fantasy.fragments.notification.NotificationFragment;
import com.football.models.responses.LeagueResponse;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;
import static com.football.services.NotificationKey.BEFORE_START_TIME_2H;
import static com.football.services.NotificationKey.BEFORE_TEAM_SETUP_TIME_1H;
import static com.football.services.NotificationKey.BEFORE_TEAM_SETUP_TIME_2H;
import static com.football.services.NotificationKey.BEFORE_TRANSFER_DEADLINE_2H;
import static com.football.services.NotificationKey.CANCEL_LEAGUE_SINCE_LACK_MEMBER;
import static com.football.services.NotificationKey.CANCEL_LEAGUE_SINCE_OWNER;
import static com.football.services.NotificationKey.CHANGE_LEAGUE_NAME;
import static com.football.services.NotificationKey.CHANGE_OWNER_LEAGUE;
import static com.football.services.NotificationKey.CHANGE_TEAM_NAME;
import static com.football.services.NotificationKey.COMPLETE_SETUP_TEAM;
import static com.football.services.NotificationKey.EDIT_LEAGUE;
import static com.football.services.NotificationKey.FULL_TEAM;
import static com.football.services.NotificationKey.LEAGUE_FINISH;
import static com.football.services.NotificationKey.LEAGUE_FINISH_FOR_CHAMPION;
import static com.football.services.NotificationKey.NEWEST_GAME_RESULT;
import static com.football.services.NotificationKey.NEWEST_REAL_RESULT;
import static com.football.services.NotificationKey.NEW_TRADE_PROPOSAL;
import static com.football.services.NotificationKey.OWNER_DELETE_MEMBER;
import static com.football.services.NotificationKey.PLAYER_HAS_LEFT_DRAFT;
import static com.football.services.NotificationKey.PLAYER_HAS_LEFT_TRANSFER;
import static com.football.services.NotificationKey.PLAYER_INJURED;
import static com.football.services.NotificationKey.PLAYER_NEW_JOIN;
import static com.football.services.NotificationKey.RANDOM_TEAM;
import static com.football.services.NotificationKey.SCORE_OF_REAL_MATCH_HAS_BEEN_ADJUSTED;
import static com.football.services.NotificationKey.START_LEAGUE;
import static com.football.services.NotificationKey.TEAM_SETUP_TIME;
import static com.football.services.NotificationKey.TIME_REAL_MATCH_CHANGED;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_APPROVED;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_CANCELLED;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_INVALID;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_REJECTED;
import static com.football.services.NotificationKey.TRANSACTION_RESULT;
import static com.football.services.NotificationKey.TWO_HOURS_TO_REVIEW;
import static com.football.services.NotificationKey.TWO_HOURS_TO_TRADE_PROPOSAL_DEADLINE;
import static com.football.services.NotificationKey.USER_ACCEPT_INVITE;
import static com.football.services.NotificationKey.USER_JOINED_LEAGUE;
import static com.football.services.NotificationKey.USER_LEFT_LEAGUE;
import static com.football.services.NotificationKey.USER_RECEIVE_INVITE;
import static com.football.services.NotificationKey.USER_REJECT_INVITE;
import static com.football.services.NotificationKey.USER_TRANSACTION_RESULT;
import static com.football.services.NotificationKey.VALUE_OF_PLAYER_CHANGED;
import static com.football.utilities.ServiceConfig.DEEP_LINK;

public class MainActivity extends BaseActivity {

    public static final String KEY_HAS_NOTIFICATION = "HAS_NOTIFICATION";

    public static final String KEY_ACTION = "action";
    public static final String KEY_TEAM_NAME = "team_name";
    public static final String KEY_LEAGUE_ID = "league_id";
    public static final String KEY_LEAGUE_STATUS = "league_status";
    public static final String KEY_MY_TEAM_ID = "my_team_id";
    public static final String KEY_TEAM_ID = "team_id";
    public static final String KEY_PLAYER_ID = "player_id";
    public static final String KEY_TRADE_ID = "trade_id";

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

    private StatePagerAdapter mPagerAdapter;
    private Handler mHandler = new Handler();
    private BroadcastReceiver broadcastReceiver;

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
        initBroadcast();

        // register socket
        getAppContext().connectSocket();

        getDeepLink(getIntent());
        handleIntent(getIntent());
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(broadcastReceiver);
        getAppContext().disconnect();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mCompositeDisposable.add(dataModule.getApiService().getNotificationsUnread()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    updateNotificationState(response.getResponse().getTotal());
                }, throwable -> {

                }));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getDeepLink(intent);
        handleIntent(intent);
    }

    private void getDeepLink(Intent intent) {
        if (intent.getDataString() != null && intent.getDataString().startsWith(DEEP_LINK)) {
            String deepLinkQuery = intent.getData().getEncodedQuery();
            if (!TextUtils.isEmpty(deepLinkQuery)) {
                Map<String, String> data = StringUtils.stringToMap(deepLinkQuery, "&");
                String leagueId = data.get("league_id");
                if (TextUtils.isDigitsOnly(leagueId)) {
                    AloneFragmentActivity.with(this)
                            .parameters(LeagueDetailFragment.newBundle(getString(R.string.my_leagues), Integer.parseInt(leagueId), LeagueDetailFragment.MY_LEAGUES))
                            .start(LeagueDetailFragment.class);
                }
            } else {
                showMessage(getString(R.string.deep_link_error), R.string.ok, null);
            }
        }
    }

    private void handleIntent(Intent intent) {
        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra(KEY_ACTION))) {
            String action = intent.getStringExtra(KEY_ACTION);
            String teamName = intent.getStringExtra(KEY_TEAM_NAME);
            int leagueId = getInteger(intent, KEY_LEAGUE_ID);
            int leagueStatus = getInteger(intent, KEY_LEAGUE_STATUS);
            int myTeamId = getInteger(intent, KEY_MY_TEAM_ID);
            int teamId = getInteger(intent, KEY_TEAM_ID);
            int playerId = getInteger(intent, KEY_PLAYER_ID);
            int tradeId = getInteger(intent, KEY_TRADE_ID);

            mHandler.postDelayed(() -> {
                handleAction(action, leagueId, leagueStatus, teamId, teamName, myTeamId, playerId, tradeId);
            }, 150);
        }
    }

    public void handleAction(String action, int leagueId, int leagueStatus, int teamId,
                             String teamName, int myTeamId, int playerId, int tradeId) {
        switch (action) {
            // League detail
            case USER_LEFT_LEAGUE:
            case BEFORE_START_TIME_2H:
            case EDIT_LEAGUE:
            case CHANGE_LEAGUE_NAME:
            case CHANGE_TEAM_NAME: // action này chưa hiểu lắm
            case BEFORE_TRANSFER_DEADLINE_2H:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.home), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            // League detail - Tab team
            case USER_JOINED_LEAGUE:
            case USER_ACCEPT_INVITE:
            case USER_REJECT_INVITE:
            case CHANGE_OWNER_LEAGUE:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.home), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            // League detail - ranking
            case LEAGUE_FINISH:
            case LEAGUE_FINISH_FOR_CHAMPION:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.home), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            // League invitation
            case USER_RECEIVE_INVITE:
                viewPager.setCurrentItem(LEAGUES);
                if (mPagerAdapter.getItem(LEAGUES) instanceof LeagueFragment) {
                    ((LeagueFragment) mPagerAdapter.getItem(LEAGUES)).openInvitation();
                }
                break;

            // Setup team - screen Lineup
            case TEAM_SETUP_TIME:
            case BEFORE_TEAM_SETUP_TIME_2H:
            case RANDOM_TEAM:
            case COMPLETE_SETUP_TEAM:   // Setup team - tab team list
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.my_leagues), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            // Setup team - tab team list
            case FULL_TEAM:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.my_leagues), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            // Edit league
            case BEFORE_TEAM_SETUP_TIME_1H:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.my_leagues), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            // My league
            case CANCEL_LEAGUE_SINCE_LACK_MEMBER:
            case CANCEL_LEAGUE_SINCE_OWNER:
            case OWNER_DELETE_MEMBER:
                viewPager.setCurrentItem(LEAGUES);
                if (mPagerAdapter.getItem(LEAGUES) instanceof LeagueFragment) {
                    ((LeagueFragment) mPagerAdapter.getItem(LEAGUES)).openMyLeague();
                }
                break;

            // Matchup - real league
            case START_LEAGUE:
            case NEWEST_REAL_RESULT:
            case TIME_REAL_MATCH_CHANGED:
            case SCORE_OF_REAL_MATCH_HAS_BEEN_ADJUSTED:
                viewPager.setCurrentItem(MATCH_UP);
                if (mPagerAdapter.getItem(MATCH_UP) instanceof MatchUpFragment) {
                    ((MatchUpFragment) mPagerAdapter.getItem(MATCH_UP)).openRealLeague();
                }
                break;

            // Matchup - my league
            case NEWEST_GAME_RESULT:
                viewPager.setCurrentItem(MATCH_UP);
                if (mPagerAdapter.getItem(MATCH_UP) instanceof MatchUpFragment) {
                    ((MatchUpFragment) mPagerAdapter.getItem(MATCH_UP)).openMyLeague();
                }
                break;

            // Player detail
            case PLAYER_NEW_JOIN:
            case VALUE_OF_PLAYER_CHANGED:
                PlayerDetailFragment.start(
                        this,
                        playerId,
                        -1,
                        getString(R.string.home),
                        PlayerDetailFragment.PICK_NONE_INFO,
                        GAMEPLAY_OPTION_TRANSFER);
                break;

            // Team squad
            case PLAYER_INJURED:

                // Accept/Reject screen
            case NEW_TRADE_PROPOSAL:
            case TWO_HOURS_TO_TRADE_PROPOSAL_DEADLINE:

                // Trade proposal - List - tab request to you
            case TRADE_PROPOSAL_CANCELLED:
            case TRADE_PROPOSAL_REJECTED:
            case TRADE_PROPOSAL_INVALID:
                // TeamSquad -> TradeRequestFragment -> RequestFragment
                AloneFragmentActivity.with(this)
                        .parameters(TeamSquadFragment.newBundle(getString(R.string.home), myTeamId, teamId, teamName, leagueStatus, action, leagueId))
                        .start(TeamSquadFragment.class);
                break;

            // League detail - trade review
            case TRANSACTION_RESULT:
            case USER_TRANSACTION_RESULT:
            case TRADE_PROPOSAL_APPROVED:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotification(getString(R.string.home), leagueId, action))
                        .start(LeagueDetailFragment.class);
                break;

            case TWO_HOURS_TO_REVIEW:
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundleForNotificationTradePreview(getString(R.string.home), leagueId, action, tradeId))
                        .start(LeagueDetailFragment.class);
                break;

            // player pool
            case PLAYER_HAS_LEFT_DRAFT:
                PlayerPoolFragment.startForNotification(this, getString(R.string.home), leagueId, teamId,
                        LeagueResponse.GAMEPLAY_OPTION_DRAFT, PLAYER_HAS_LEFT_DRAFT);
                break;

            // player pool
            case PLAYER_HAS_LEFT_TRANSFER:
                PlayerPoolFragment.startForNotification(this, getString(R.string.home), leagueId, teamId,
                        LeagueResponse.GAMEPLAY_OPTION_TRANSFER, PLAYER_HAS_LEFT_TRANSFER);
                break;

            default:
                if (BuildConfig.DEBUG)
                    Toast.makeText(this, "Chưa làm action " + action, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private int getInteger(Intent intent, String key) {
        if (intent.hasExtra(key)) {
            int value = intent.getIntExtra(key, -1);
            if (value == -1) {
                String string = intent.getStringExtra(key);
                value = !TextUtils.isEmpty(string) && TextUtils.isDigitsOnly(string) ? Integer.parseInt(string) : -1;
            }
            return value;
        }
        return -1;
    }

    private void initViewPager() {
        mPagerAdapter = new StatePagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(HomeFragment.newInstance());
        mPagerAdapter.addFragment(LeagueFragment.newInstance());
        mPagerAdapter.addFragment(MatchUpFragment.newInstance());
        mPagerAdapter.addFragment(NotificationFragment.newInstance());
        mPagerAdapter.addFragment(MoreFragment.newInstance());
        viewPager.setAdapter(mPagerAdapter);
//        viewPager.setOffscreenPageLimit(5);
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

    private void initRxBus() {
        onEvent(UnauthorizedEvent.class, res -> {
            DialogUtils.messageBox(MainActivity.this,
                    getString(R.string.app_name),
                    res.getMessage(),
                    getString(R.string.ok),
                    (dialog, which) -> {
                        ActivityUtils.startActivity(AccountActivity.class);
                        AppPreferences.getInstance(getAppContext()).clearCache();
                        MainActivity.this.finish();
                    });
        });
    }

    private void initBroadcast() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(KEY_HAS_NOTIFICATION);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction() != null && intent.getAction().equals(KEY_HAS_NOTIFICATION)) {
                    updateNotificationState(1);
                }
                Log.d("initBroadcast", "onReceive: ");
            }
        };

        registerReceiver(broadcastReceiver, intentFilter);
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
        footerLeagues.setActiveMode(this, currentTab == LEAGUES);
        footerMatchUp.setActiveMode(this, currentTab == MATCH_UP);
        footerNotification.setActiveMode(this, currentTab == NOTIFICATION);
        footerMore.setActiveMode(this, currentTab == MORE);
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

    public void updateNotificationState(int total) {
        footerNotification.setNotification(total > 0);
    }
}
