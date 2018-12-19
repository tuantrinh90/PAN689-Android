package com.football.fantasy.fragments.home;

import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.bon.customview.textview.ExtTextView;
import com.bon.logger.Logger;
import com.bon.share_preferences.AppPreferences;
import com.bon.util.StringUtils;
import com.bon.util.ViewUtils;
import com.football.adapters.MyLeagueRecyclerAdapter;
import com.football.adapters.NewsAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.fantasy.fragments.leagues.action.setup_leagues.SetUpLeagueFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.fantasy.fragments.leagues.player_pool.PlayerPoolFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.NewsResponse;
import com.football.utilities.Constant;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMainMvpFragment<IHomeView, IHomePresenter<IHomeView>> implements IHomeView, ViewTreeObserver.OnGlobalLayoutListener {
    private static final String TAG = HomeFragment.class.getSimpleName();

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.tvFootballLeagues)
    ExtTextView tvFootballLeagues;
    @BindView(R.id.tvCreateLeagues)
    ExtTextView tvCreateLeagues;
    @BindView(R.id.tvJoinLeagues)
    ExtTextView tvJoinLeagues;
    @BindView(R.id.llPlayerList)
    ViewGroup llPlayerList;
    @BindView(R.id.tvMyLeagues)
    ExtTextView tvMyLeagues;
    @BindView(R.id.rvMyLeagues)
    RecyclerView rvMyLeagues;
    @BindView(R.id.tvNews)
    ExtTextView tvNews;
    @BindView(R.id.rvNews)
    RecyclerView rvNews;

    @BindView(R.id.refresh)
    SwipeRefreshLayout refresh;

    NewsAdapter newsAdapter;
    List<NewsResponse> newsResponses;

    MyLeagueRecyclerAdapter myLeagueRecyclerAdapter;
    List<LeagueResponse> leagueResponses;

    @Override
    public int getResourceId() {
        return R.layout.home_fragment;
    }

    @Override
    protected void initialized() {
        super.initialized();
        registerNotification();
        registerEvent();

        initView();

        // update language
        presenter.updateLanguages(AppPreferences.getInstance(getAppContext()).getString(Constant.KEY_LANGUAGE));

        // get notifications count
        getNotificationsCount();

        // load my leagues, only display 5 records
        getMyLeagues();

        // load news
        loadNews();
    }

    void registerNotification() {
        FirebaseMessaging.getInstance().setAutoInitEnabled(true);
        // Get token
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnSuccessListener(mActivity, result -> {
                    String deviceId = Settings.Secure.getString(getContext().getContentResolver(),
                            Settings.Secure.ANDROID_ID);

                    // Get new Instance ID token
                    String token = result.getToken();
                    presenter.updateDeviceOfUser(deviceId, token);
                });
    }

    void registerEvent() {
        // load my leagues
        onEvent(LeagueEvent.class, res -> getMyLeagues());

        // load my leagues, remove
        onEvent(StopLeagueEvent.class, res -> getMyLeagues());
    }

    void initView() {
        initRecyclerView();
        refresh.setOnRefreshListener(() -> {
            getMyLeagues();
            loadNews();
        });
    }

    private void getNotificationsCount() {
        presenter.getNotificationsCount();
    }

    private void getMyLeagues() {
        presenter.getMyLeagues(1);
    }

    void initRecyclerView() {
        try {
            newsAdapter = new NewsAdapter(mActivity, newsResponses, item -> {
                String url = item.getUrl();
                if (StringUtils.isEmpty(url)) return;

                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(mActivity, R.color.colorPrimary));
                CustomTabsIntent customTabsIntent = builder.build();
                customTabsIntent.launchUrl(mActivity, Uri.parse(url));
            });
            rvNews.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
            rvNews.setAdapter(newsAdapter);

            // leagueResponses
            myLeagueRecyclerAdapter = new MyLeagueRecyclerAdapter(mActivity, leagueResponses, league -> {
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundle(getString(R.string.my_leagues), league.getId(), LeagueDetailFragment.MY_LEAGUES))
                        .start(LeagueDetailFragment.class);
            });
            rvMyLeagues.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
            rvMyLeagues.setAdapter(myLeagueRecyclerAdapter);

            // center view
            SnapHelper gravitySnapHelper = new LinearSnapHelper();
            gravitySnapHelper.attachToRecyclerView(rvMyLeagues);
        } catch (IllegalStateException e) {
            Logger.e(TAG, e);
        }
    }

    private void loadNews() {
        presenter.getNews(1);
    }

    @NonNull
    @Override
    public IHomePresenter<IHomeView> createPresenter() {
        return new HomeDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvCreateLeagues)
    void onClickCreateLeagues() {
        AloneFragmentActivity
                .with(this)
                .parameters(SetUpLeagueFragment.newBundle(null, getString(R.string.home)))
                .start(SetUpLeagueFragment.class);
    }

    @OnClick(R.id.tvJoinLeagues)
    void onClickJoinLeagues() {
        mMainActivity.openOpenLeagueFromLeague();
    }

    @OnClick(R.id.llPlayerList)
    void onClickPlayerList() {
        PlayerPoolFragment.start(getContext(), getString(R.string.home));
    }

    @OnClick(R.id.tvMyLeagues)
    void onClickMyLeagues() {
        mMainActivity.onClickFooter(MainActivity.LEAGUES);
    }

    @OnClick(R.id.tvNews)
    void onClickNews() {
    }

    @Override
    public void onGlobalLayout() {
        ViewUtils.autoLayoutScale(llPlayerList, 2.26f);
        ViewUtils.detachViewTreeObserver(llPlayerList, this);
    }

    @Override
    public void updateNotificationState(int total) {
        mMainActivity.updateNotificationState(total);
    }

    @Override
    public void notifyDataSetChangedNews(List<NewsResponse> its) {
        refresh.setRefreshing(false);
        try {
            rvNews.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
            newsAdapter.notifyDataSetChanged(its);
            tvNews.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void notifyDataSetChangedLeagues(List<LeagueResponse> its) {
        refresh.setRefreshing(false);
        try {
            rvMyLeagues.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
            myLeagueRecyclerAdapter.notifyDataSetChanged(its);
            tvMyLeagues.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void stopLoading() {
        refresh.setRefreshing(false);
    }
}
