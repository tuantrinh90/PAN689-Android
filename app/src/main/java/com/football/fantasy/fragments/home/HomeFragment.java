package com.football.fantasy.fragments.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.football.adapters.MyLeagueRecyclerAdapter;
import com.football.adapters.NewsRecyclerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.fantasy.fragments.leagues.action.ActionLeagueFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMainMvpFragment<IHomeView, IHomePresenter<IHomeView>> implements IHomeView, ViewTreeObserver.OnGlobalLayoutListener {
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

    NewsRecyclerAdapter newsRecyclerAdapter;
    List<News> news;

    MyLeagueRecyclerAdapter myLeagueRecyclerAdapter;
    List<LeagueResponse> leagueResponses;

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
        presenter.getMyLeagues();
    }

    void initRecyclerView() {
        // news
        news = new ArrayList<News>() {{
            add(new News("20", "May", getString(R.string.forgot_description_success)));
            add(new News("19", "May", getString(R.string.forgot_description_success)));
            add(new News("18", "May", getString(R.string.forgot_description_success)));
            add(new News("17", "May", getString(R.string.forgot_description_success)));
        }};

        newsRecyclerAdapter = new NewsRecyclerAdapter(mActivity, news, item -> {
        });
        rvNews.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvNews.setAdapter(newsRecyclerAdapter);

        // leagueResponses
        myLeagueRecyclerAdapter = new MyLeagueRecyclerAdapter(mActivity, leagueResponses, league -> {
        });
        rvMyLeagues.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvMyLeagues.setAdapter(myLeagueRecyclerAdapter);

        // center view
        SnapHelper gravitySnapHelper = new LinearSnapHelper();
        gravitySnapHelper.attachToRecyclerView(rvMyLeagues);
    }

    @NonNull
    @Override
    public IHomePresenter<IHomeView> createPresenter() {
        return new HomeDataPresenter(getAppComponent());
    }

    @OnClick(R.id.tvCreateLeagues)
    void onClickCreateLeagues() {
        AloneFragmentActivity.with(this).start(ActionLeagueFragment.class);
    }

    @OnClick(R.id.tvJoinLeagues)
    void onClickJoinLeagues() {
        mMainActivity.onClickFooter(MainActivity.LEAGUES);
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
