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
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

import java.util.List;

import butterknife.BindView;

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
            Bundle bundle = new Bundle();
            bundle.putString(LeagueDetailFragment.KEY_TITLE, getString(R.string.open_leagues));
            bundle.putInt(LeagueDetailFragment.KEY_LEAGUE_ID, details.getId());
            AloneFragmentActivity.with(this)
                    .parameters(bundle)
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
