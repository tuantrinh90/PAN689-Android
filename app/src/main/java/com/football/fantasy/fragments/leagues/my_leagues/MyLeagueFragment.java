package com.football.fantasy.fragments.leagues.my_leagues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

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

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<LeagueResponse> leagueResponses;
    LeaguesAdapter leaguesAdapter;

    int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.my_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
        registerEvent();
    }

    void registerEvent() {
        try {
            // load my leagues
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class).subscribeWith(new DisposableObserver<LeagueEvent>() {
                @Override
                public void onNext(LeagueEvent leagueEvent) {
                    try {
                        Log.e("LeagueEvent","LeagueEvent");
                        page = 1;
                        rvRecyclerView.clearItems();
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
                                List<LeagueResponse> leagueResponses = leaguesAdapter.getItems();
                                if (leagueResponses != null && leagueResponses.size() > 0) {
                                    leagueResponses = StreamSupport.stream(leagueResponses).filter(n -> n.getId() != stopLeagueEvent.getLeagueId()).collect(Collectors.toList());
                                    rvRecyclerView.notifyDataSetChanged(leagueResponses);
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
        rvRecyclerView.setMessage(getString(R.string.loading));
        presenter.getMyLeagues(page, ExtPagingListView.NUMBER_PER_PAGE);
    }

    void initView() {
        try {
            // leagueResponses
            leaguesAdapter = new LeaguesAdapter(mActivity, LeaguesAdapter.MY_LEAGUES, leagueResponses, details -> {
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundle(getString(R.string.my_leagues), details.getId(), LeagueDetailFragment.MY_LEAGUES))
                        .start(LeagueDetailFragment.class);
            }, null, null, null);
            rvRecyclerView.init(mActivity, leaguesAdapter)
                    .setOnExtRefreshListener(() -> {
                        try {
                            page = 1;
                            rvRecyclerView.clearItems();
                            getMyLeagues();
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    })
                    .setOnExtLoadMoreListener(() -> {
                        try {
                            page++;
                            getMyLeagues();
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    });

            // load data
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
    public void notifyDataSetChangedLeagues(List<LeagueResponse> its) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(its));
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            if (isLoading) {
                rv.startLoading(true);
            } else {
                rv.stopLoading(true);
            }
        });
    }
}
