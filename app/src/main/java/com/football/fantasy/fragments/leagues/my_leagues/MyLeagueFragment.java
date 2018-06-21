package com.football.fantasy.fragments.leagues.my_leagues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class MyLeagueFragment extends BaseMainMvpFragment<IMyLeagueView, IMyLeaguePresenter<IMyLeagueView>> implements IMyLeagueView {
    static final String TAG = MyLeagueFragment.class.getSimpleName();

    public static MyLeagueFragment newInstance() {
        return new MyLeagueFragment();
    }

    @BindView(R.id.rv_league)
    ExtRecyclerView<LeagueResponse> rvLeague;

    private boolean initialized = false;

    LeaguesAdapter mAdapter;

    int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.my_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !initialized) {
            initialized = true;
            initView();
            registerEvent();
        }
    }

    void registerEvent() {
        try {
            // load my leagues
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class).subscribeWith(new DisposableObserver<LeagueEvent>() {
                @Override
                public void onNext(LeagueEvent leagueEvent) {
                    try {
                        Log.e("LeagueEvent", "LeagueEvent");
                        page = 1;
                        rvLeague.clear();
                        rvLeague.startLoading();
                        getMyLeagues();
                    } catch (Exception e) {
                        Logger.e(TAG, e);
                    }
                }

                @Override
                public void onError(Throwable e) {

                }

                @Override
                public void onComplete() {

                }
            }));

            // load my leagues, remove
            mCompositeDisposable.add(bus.ofType(StopLeagueEvent.class)
                    .subscribeWith(new DisposableObserver<StopLeagueEvent>() {
                        @Override
                        public void onNext(StopLeagueEvent stopLeagueEvent) {
                            try {
                                List<LeagueResponse> leagues = mAdapter.getDataSet();
                                if (leagues != null && leagues.size() > 0) {
                                    leagues = StreamSupport.stream(leagues).filter(n -> n.getId() != stopLeagueEvent.getLeagueId()).collect(Collectors.toList());
                                    rvLeague.clear();
                                    rvLeague.addItems(leagues);
                                }
                            } catch (Exception e) {
                                Logger.e(TAG, e);
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void getMyLeagues() {
        presenter.getMyLeagues(page, ExtPagingListView.NUMBER_PER_PAGE);
    }

    void initView() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));

        try {
            // leagueResponses
            mAdapter = new LeaguesAdapter(
                    LeaguesAdapter.MY_LEAGUES,
                    new ArrayList<>(),
                    details -> {
                        AloneFragmentActivity.with(this)
                                .parameters(LeagueDetailFragment.newBundle(getString(R.string.my_leagues), details.getId(), LeagueDetailFragment.MY_LEAGUES))
                                .start(LeagueDetailFragment.class);
                    },
                    null,
                    null,
                    null);
            rvLeague.
                    adapter(mAdapter)
                    .refreshListener(() -> {
                        rvLeague.clear();
                        rvLeague.startLoading();
                        page = 1;
                        getMyLeagues();
                    })
                    .loadMoreListener(() -> {
                        page++;
                        getMyLeagues();
                    })
                    .build();
            // load data
            rvLeague.startLoading();
            getMyLeagues();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public IMyLeaguePresenter<IMyLeagueView> createPresenter() {
        return new MyLeagueDataPresenter(getAppComponent());
    }

    @Override
    public void displayLeagues(List<LeagueResponse> its) {
        rvLeague.addItems(its);
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
    }
}
