package com.football.fantasy.fragments.leagues.my_leagues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

import java.util.List;

import butterknife.BindView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;

public class MyLeagueFragment extends BaseMainMvpFragment<IMyLeagueView, IMyLeaguePresenter<IMyLeagueView>> implements IMyLeagueView {
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
    }

    void initView() {
        // leagueResponses
        leaguesAdapter = new LeaguesAdapter(mActivity, LeaguesAdapter.MY_LEAGUES, leagueResponses, details -> {
            AloneFragmentActivity.with(this)
                    .parameters(LeagueDetailFragment.newBundle(getString(R.string.my_leagues), details.getId(), LeagueDetailFragment.MY_LEAGUES))
                    .start(LeagueDetailFragment.class);
        }, null, null, null);
        rvRecyclerView.init(mActivity, leaguesAdapter)
                .setOnExtRefreshListener(() -> {
                    page = 1;
                    rvRecyclerView.clearItems();
                    presenter.getMyLeagues(page, ExtPagingListView.NUMBER_PER_PAGE);
                })
                .setOnExtLoadMoreListener(() -> {
                    page++;
                    presenter.getMyLeagues(page, ExtPagingListView.NUMBER_PER_PAGE);
                });

        // load data
        presenter.getMyLeagues(page, ExtPagingListView.NUMBER_PER_PAGE);

        subscribeRxBus();
    }

    void subscribeRxBus() {
        mCompositeDisposable.add(bus.ofType(StopLeagueEvent.class).observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<StopLeagueEvent>() {
                    @Override
                    public void onNext(StopLeagueEvent stopLeagueEvent) {
                        List<LeagueResponse> leagueResponses = leaguesAdapter.getItems();
                        if (leagueResponses != null && leagueResponses.size() > 0) {
                            LeagueResponse leagueResponse = null;
                            for (int i = 0; i < leagueResponses.size(); i++) {
                                if (leagueResponses.get(i).getId() == stopLeagueEvent.getLeagueId()) {
                                    leagueResponse = leagueResponses.get(i);
                                    break;
                                }
                            }

                            if (leagueResponse != null) {
                                leaguesAdapter.removeItem(leagueResponse);
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
