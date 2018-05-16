package com.football.fantasy.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.ViewUtils;
import com.football.adapters.MyLeagueAdapter;
import com.football.adapters.NewsAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.League;
import com.football.models.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMvpFragment<IHomeView, IHomePresenter<IHomeView>> implements IHomeView, ViewTreeObserver.OnGlobalLayoutListener {
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
    LinearLayout llPlayerList;
    @BindView(R.id.tvPlayerList)
    ExtTextView tvPlayerList;
    @BindView(R.id.tvMyLeagues)
    ExtTextView tvMyLeagues;
    @BindView(R.id.rvMyLeagues)
    RecyclerView rvMyLeagues;
    @BindView(R.id.tvNews)
    ExtTextView tvNews;
    @BindView(R.id.rvNews)
    RecyclerView rvNews;

    NewsAdapter newsAdapter;
    List<News> news;

    MyLeagueAdapter myLeagueAdapter;
    List<League> leagues;

    @Override
    public int getResourceId() {
        return R.layout.home_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        initRecyclerView();
        ViewUtils.attachViewTreeObserver(llPlayerList, this);
    }

    void initRecyclerView() {
        // news
        news = new ArrayList<News>() {{
            add(new News("20", "May", getString(R.string.forgot_description_success)));
            add(new News("19", "May", getString(R.string.forgot_description_success)));
            add(new News("18", "May", getString(R.string.forgot_description_success)));
            add(new News("17", "May", getString(R.string.forgot_description_success)));
        }};

        newsAdapter = new NewsAdapter(mActivity, news, item -> {
        });
        rvNews.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvNews.setAdapter(newsAdapter);

        // leagues
        leagues = new ArrayList<League>(){{
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
            add(new League("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "Lorem ipsum dolor", "Join Hard", "Invitor", 2, 8, "1 hour to Draft Time", 1, 1));
        }};
        myLeagueAdapter= new MyLeagueAdapter(mActivity, leagues, league -> {

        });
        rvMyLeagues.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvMyLeagues.setAdapter(myLeagueAdapter);

        // center view
        SnapHelper gravitySnapHelper = new LinearSnapHelper();
        gravitySnapHelper.attachToRecyclerView(rvMyLeagues);
    }

    @Override
    public IHomePresenter<IHomeView> createPresenter() {
        return new HomePresenter(getAppComponent());
    }

    @OnClick(R.id.tvCreateLeagues)
    void onClickCreateLeagues() {
    }

    @OnClick(R.id.tvJoinLeagues)
    void onClickJoinLeagues() {
    }

    @OnClick(R.id.tvPlayerList)
    void onClickPlayerList() {
    }

    @OnClick(R.id.tvMyLeagues)
    void onClickMyLeagues() {
    }

    @OnClick(R.id.tvNews)
    void onClickNews() {
    }

    @Override
    public void onGlobalLayout() {
        ViewUtils.autoLayoutScale(llPlayerList, 2.26f);
        ViewUtils.detachViewTreeObserver(llPlayerList, this);
    }
}
