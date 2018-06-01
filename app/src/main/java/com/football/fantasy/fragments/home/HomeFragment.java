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

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.ViewUtils;
import com.football.adapters.MyLeagueRecyclerAdapter;
import com.football.adapters.NewsAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.activities.MainActivity;
import com.football.fantasy.fragments.leagues.action.ActionLeagueFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.NewsResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

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

    NewsAdapter newsAdapter;
    List<NewsResponse> newsResponses;

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
    }

    void initRecyclerView() {
        newsAdapter = new NewsAdapter(mActivity, newsResponses, item -> {

        });
        rvNews.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvNews.setAdapter(newsAdapter);

        // leagueResponses
        myLeagueRecyclerAdapter = new MyLeagueRecyclerAdapter(mActivity, leagueResponses, league -> {
            Bundle bundle = new Bundle();
            bundle.putString(LeagueDetailFragment.KEY_TITLE, getString(R.string.open_leagues));
            bundle.putInt(LeagueDetailFragment.KEY_LEAGUE_ID, league.getId());
            bundle.putString(LeagueDetailFragment.KEY_LEAGUE_TYPE, LeagueDetailFragment.MY_LEAGUES);
            AloneFragmentActivity.with(this)
                    .parameters(bundle)
                    .start(LeagueDetailFragment.class);
        });
        rvMyLeagues.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.HORIZONTAL, false));
        rvMyLeagues.setAdapter(myLeagueRecyclerAdapter);

        // center view
        SnapHelper gravitySnapHelper = new LinearSnapHelper();
        gravitySnapHelper.attachToRecyclerView(rvMyLeagues);

        // load my leagues
        presenter.getMyLeagues(1, ExtPagingListView.NUMBER_PER_PAGE);

        // load news
        presenter.getNews(1, ExtPagingListView.NUMBER_PER_PAGE);

        subscribeRxBus();
    }

    void subscribeRxBus() {
        mCompositeDisposable.add(bus.ofType(StopLeagueEvent.class).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<StopLeagueEvent>() {
                    @Override
                    public void onNext(StopLeagueEvent stopLeagueEvent) {
                        List<LeagueResponse> leagueResponses = myLeagueRecyclerAdapter.getItems();
                        if (leagueResponses != null && leagueResponses.size() > 0) {
                            int index = -1;
                            for (int i = 0; i < leagueResponses.size(); i++) {
                                if (leagueResponses.get(i).getId() == stopLeagueEvent.getLeagueId()) {
                                    index = i;
                                    break;
                                }
                            }

                            if (index >= 0) {
                                myLeagueRecyclerAdapter.notifyItemRemoved(index);
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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
    public void notifyDataSetChangedNews(List<NewsResponse> its) {
        rvNews.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
        newsAdapter.notifyDataSetChanged(its);
        tvNews.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void notifyDataSetChangedLeagues(List<LeagueResponse> its) {
        rvMyLeagues.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
        myLeagueRecyclerAdapter.notifyDataSetChanged(its);
        tvMyLeagues.setVisibility(its != null && its.size() > 0 ? View.VISIBLE : View.GONE);
    }
}
