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

    private String orderBy = "desc";
    private int page;
    private String query = "";
    private String numberOfUser = Constant.NUMBER_OF_USER_ALL;

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !numberOfUser.equals(Constant.NUMBER_OF_USER_ALL)) {
            numberOfUser = Constant.NUMBER_OF_USER_ALL;
            getOpenLeagues();
        }
    }

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
        // load my leagues
        onEvent(LeagueEvent.class, event -> refresh());

        // load my leagues, remove
        onEvent(StopLeagueEvent.class, event -> reloadLeague(event.getLeagueId()));
    }

    private void reloadLeague(int leagueId) {
        try {
            List<LeagueResponse> leagues = mAdapter.getDataSet();
            if (leagues != null && leagues.size() > 0) {
                leagues = StreamSupport.stream(leagues).filter(n -> n != null && n.getId() != leagueId).collect(Collectors.toList());
                rvLeague.clear();
                rvLeague.addItems(leagues);
                rvLeague.removeLoading();
            }
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
                    getContext(),
                    LeaguesAdapter.OPEN_LEAGUES,
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
        valuePairs.add(new ExtKeyValuePair(Constant.NUMBER_OF_USER_ALL, getString(R.string.none)));
        valuePairs.add(new ExtKeyValuePair("4", "4"));
        valuePairs.add(new ExtKeyValuePair("6", "6"));
        valuePairs.add(new ExtKeyValuePair("8", "8"));
        valuePairs.add(new ExtKeyValuePair("10", "10"));
        valuePairs.add(new ExtKeyValuePair("12", "12"));

        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(numberOfUser)
                .title(getString(R.string.select_number_of_user))
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        numberOfUser = extKeyValuePair.getKey();
                        refresh();
                    }
                }).show(getChildFragmentManager(), null);
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
    public void stopLoading() {
        rvLeague.stopLoading();
    }

    @Override
    public void onJoinSuccessful(LeagueResponse league) {
        // refresh my league
        bus.send(new LeagueEvent());

        AloneFragmentActivity.with(this)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        league.getId(),
                        getString(R.string.open_leagues)))
                .start(SetupTeamFragment.class);
    }

}
