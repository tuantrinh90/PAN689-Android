package com.football.fantasy.fragments.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.NewsAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeFragment extends BaseMvpFragment<IHomeView, IHomePresenter<IHomeView>> implements IHomeView {
    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @BindView(R.id.tvFootballLeagues)
    ExtTextView tvFootballLeagues;
    @BindView(R.id.tvCreateLeagues)
    ExtTextView tvCreateLeagues;
    @BindView(R.id.tvJoinLeagues)
    ExtTextView tvJoinLeagues;
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

    @Override
    public int getResourceId() {
        return R.layout.home_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initRecyclerView();
    }

    void initRecyclerView() {
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
}
