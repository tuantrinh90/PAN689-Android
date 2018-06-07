package com.football.fantasy.fragments.leagues.open_leagues;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.GeneralUtils;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;
import java8.util.stream.Collectors;
import java8.util.stream.StreamSupport;

public class OpenLeagueFragment extends BaseMainMvpFragment<IOpenLeagueView, IOpenLeaguePresenter<IOpenLeagueView>> implements IOpenLeagueView {
    static final String TAG = OpenLeagueFragment.class.getSimpleName();

    public static OpenLeagueFragment newInstance() {
        return new OpenLeagueFragment();
    }

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    SearchView svSearchView;
    List<LeagueResponse> leagueResponses;
    LeaguesAdapter leaguesAdapter;

    String orderBy = "desc";
    int page = 1;
    String query = "";

    @Override
    public int getResourceId() {
        return R.layout.open_league_fragment;
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
                        page = 1;
                        rvRecyclerView.clearItems();
                        presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
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

    void initView() {
        try {
            // search view
            DisplayMetrics displayMetrics = GeneralUtils.getDisplayMetrics(mActivity);
            int paddingLayout = getResources().getDimensionPixelSize(R.dimen.padding_layout);
            int marginLayout = getResources().getDimensionPixelOffset(R.dimen.leagues_margin);

            svSearchView = new SearchView(mActivity);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(displayMetrics.widthPixels - paddingLayout * 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            svSearchView.setPadding(marginLayout, 0, marginLayout, marginLayout);

            //layoutParams.gravity = Gravity.CENTER;
            svSearchView.setLayoutParams(layoutParams);

            // add search view to header
            rvRecyclerView.getListView().addHeaderView(svSearchView);

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_leagues);

            // click button filter
            svSearchView.setFilerConsumer(v -> onClickFilter());

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query));

            // leagueResponses
            leaguesAdapter = new LeaguesAdapter(mActivity, LeaguesAdapter.OPEN_LEAGUES, leagueResponses, details -> {
                AloneFragmentActivity.with(this)
                        .parameters(LeagueDetailFragment.newBundle(getString(R.string.open_leagues), details.getId(), LeagueDetailFragment.OPEN_LEAGUES))
                        .start(LeagueDetailFragment.class);
            }, null, null, join -> {
                AloneFragmentActivity.with(this)
                        .parameters(SetupTeamFragment.newBundle(
                                join,
                                null,
                                getString(R.string.open_leagues),
                                LeagueDetailFragment.OPEN_LEAGUES,
                                SetupTeamFragment.INVITATION_NONE))
                        .start(SetupTeamFragment.class);
            });
            rvRecyclerView.init(mActivity, leaguesAdapter)
                    .setOnExtLoadMoreListener(() -> {
                        try {
                            page++;
                            presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    })
                    .setOnExtRefreshListener(() -> Optional.from(rvRecyclerView).doIfPresent(rv -> {
                        try {
                            page = 1;
                            rv.clearItems();
                            presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    }));

            // load data
            presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    void onClickFilter() {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            try {
                orderBy = orderBy.equalsIgnoreCase("desc") ? "asc" : "desc";
                svSearchView.getFilter().animate().rotation(orderBy.equalsIgnoreCase("desc") ? 0 : 180)
                        .setDuration(500).setInterpolator(new LinearInterpolator()).start();
                rv.clearItems();
                page = 1;
                presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        });
    }

    void onPerformSearch(String q) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            try {
                query = q;
                rv.clearItems();
                page = 1;
                presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        });
    }

    @NonNull
    @Override
    public IOpenLeaguePresenter<IOpenLeagueView> createPresenter() {
        return new OpenLeagueDataPresenter(getAppComponent());
    }

    @Override
    public void notifyDataSetChangedLeagues(List<LeagueResponse> its) {
        try {
            Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(its));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
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
