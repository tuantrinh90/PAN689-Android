package com.football.fantasy.fragments.leagues.open_leagues;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.logger.Logger;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.customizes.searchs.SearchView;
import com.football.events.LeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
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

    @BindView(R.id.rv_league)
    ExtRecyclerView<LeagueResponse> rvLeague;
    @BindView(R.id.searchView)
    SearchView svSearchView;


    private LeaguesAdapter mAdapter;

    String orderBy = "desc";
    int page;
    String query = "";
    String numberOfUser = Constant.NUMBER_OF_USER_ALL;

    @Override
    public int getResourceId() {
        return R.layout.open_league_fragment;
    }

    @Override
    protected void initialized() {
        super.initialized();
        initView();
        registerEvent();

        // load data
        page = 1;
        rvLeague.startLoading();
        getOpenLeagues();
    }

    void registerEvent() {
        try {
            // load my leagues
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class).subscribeWith(new DisposableObserver<LeagueEvent>() {
                @Override
                public void onNext(LeagueEvent leagueEvent) {
                    try {
                        page = 1;
                        rvLeague.clear();
                        rvLeague.startLoading();
                        getOpenLeagues();
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

    void initView() {
        try {
            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_leagues);

            // click button filter
            svSearchView.setFilerConsumer(v -> onClickFilter());

            // search view
            svSearchView.setSearchConsumer(query -> onPerformSearch(query.trim()));

            // leagueResponses
            mAdapter = new LeaguesAdapter(
                    LeaguesAdapter.OPEN_LEAGUES,
                    new ArrayList<>(),
                    details -> {
                        AloneFragmentActivity.with(this)
                                .parameters(LeagueDetailFragment.newBundle(getString(R.string.open_leagues), details.getId(), LeagueDetailFragment.OPEN_LEAGUES))
                                .start(LeagueDetailFragment.class);
                    },
                    null,
                    null,
                    join -> {
                        presenter.join(join.getId());
                    });
            rvLeague.
                    adapter(mAdapter)
                    .loadMoreListener(() -> {
                        page++;
                        getOpenLeagues();
                    })
                    .refreshListener(() -> {
                        refresh();
                    })
                    .build();
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    private void getOpenLeagues() {
        presenter.getOpenLeagues(orderBy, page, query, numberOfUser);
    }

    private void refresh() {
        page = 1;
        rvLeague.clear();
        rvLeague.startLoading();
        getOpenLeagues();
    }

    void onClickFilter() {
        List<ExtKeyValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair(Constant.NUMBER_OF_USER_ALL, "None"));
        valuePairs.add(new ExtKeyValuePair("4", "4"));
        valuePairs.add(new ExtKeyValuePair("6", "6"));
        valuePairs.add(new ExtKeyValuePair("8", "8"));
        valuePairs.add(new ExtKeyValuePair("10", "10"));
        valuePairs.add(new ExtKeyValuePair("12", "12"));

        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(numberOfUser)
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        numberOfUser = extKeyValuePair.getKey();
                        refresh();
                    }
                }).show(getFragmentManager(), null);
    }

    void onPerformSearch(String q) {
        try {
            query = q;
            rvLeague.clear();
            rvLeague.startLoading();
            page = 1;
            getOpenLeagues();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public IOpenLeaguePresenter<IOpenLeagueView> createPresenter() {
        return new OpenLeagueDataPresenter(getAppComponent());
    }

    @Override
    public void displayLeagues(List<LeagueResponse> its) {
        rvLeague.addItems(its);
    }

    @Override
    public void refreshData(Integer leagueId) {
        refresh();
    }

    @Override
    public void onJoinSuccessful(LeagueResponse league) {
        // refresh my league
        bus.send(new LeagueEvent());

        AloneFragmentActivity.with(this)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        league.getId(),
                        getString(R.string.open_leagues),
                        LeagueDetailFragment.OPEN_LEAGUES))
                .start(SetupTeamFragment.class);
    }

}
