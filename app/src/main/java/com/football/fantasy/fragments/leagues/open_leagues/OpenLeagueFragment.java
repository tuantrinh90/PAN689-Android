package com.football.fantasy.fragments.leagues.open_leagues;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.logger.Logger;
import com.bon.util.GeneralUtils;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class OpenLeagueFragment extends BaseMainMvpFragment<IOpenLeagueView, IOpenLeaguePresenter<IOpenLeagueView>> implements IOpenLeagueView {
    private static final String TAG = OpenLeagueFragment.class.getSimpleName();

    public static OpenLeagueFragment newInstance() {
        return new OpenLeagueFragment();
    }

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<LeagueResponse> leagueResponses;
    LeaguesAdapter leaguesAdapter;

    @Override
    public int getResourceId() {
        return R.layout.open_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        try {
            // search view
            DisplayMetrics displayMetrics = GeneralUtils.getDisplayMetrics(mActivity);
            int paddingLayout = getResources().getDimensionPixelSize(R.dimen.padding_layout);
            int marginLayout = getResources().getDimensionPixelOffset(R.dimen.leagues_margin);

            SearchView svSearchView = new SearchView(mActivity);
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(displayMetrics.widthPixels - paddingLayout * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
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
            leaguesAdapter = new LeaguesAdapter(mActivity,LeaguesAdapter.OPEN_LEAGUES, leagueResponses, details -> {
                Bundle bundle = new Bundle();
                bundle.putString(LeagueDetailFragment.KEY_TITLE, getString(R.string.open_leagues));
                bundle.putSerializable(LeagueDetailFragment.KEY_LEAGUE, details);
                AloneFragmentActivity.with(this)
                        .parameters(bundle)
                        .start(LeagueDetailFragment.class);
            }, approve -> {

            }, reject -> {

            }, join -> {

            });
            rvRecyclerView.init(mActivity, leaguesAdapter);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    void onClickFilter() {
        Log.e("onClickFilter", "onClickFilter");
    }

    void onPerformSearch(String query) {
        Log.e("onPerformSearch", "onPerformSearch");
    }

    @NonNull
    @Override
    public IOpenLeaguePresenter<IOpenLeagueView> createPresenter() {
        return new OpenLeagueDataPresenter(getAppComponent());
    }
}
