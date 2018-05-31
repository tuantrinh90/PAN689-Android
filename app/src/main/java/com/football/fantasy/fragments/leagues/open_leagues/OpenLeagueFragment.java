package com.football.fantasy.fragments.leagues.open_leagues;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.GeneralUtils;
import com.bon.view_animation.Techniques;
import com.bon.view_animation.YoYo;
import com.football.adapters.LeaguesAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.models.responses.LeagueResponse;

import java.util.List;

import butterknife.BindView;

public class OpenLeagueFragment extends BaseMainMvpFragment<IOpenLeagueView, IOpenLeaguePresenter<IOpenLeagueView>> implements IOpenLeagueView {
    private static final String TAG = OpenLeagueFragment.class.getSimpleName();

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
    }

    void initView() {
        try {
            // search view
            DisplayMetrics displayMetrics = GeneralUtils.getDisplayMetrics(mActivity);
            int paddingLayout = getResources().getDimensionPixelSize(R.dimen.padding_layout);
            int marginLayout = getResources().getDimensionPixelOffset(R.dimen.leagues_margin);

            svSearchView = new SearchView(mActivity);
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
            leaguesAdapter = new LeaguesAdapter(mActivity, LeaguesAdapter.OPEN_LEAGUES, leagueResponses, details -> {
                Bundle bundle = new Bundle();
                bundle.putString(LeagueDetailFragment.KEY_TITLE, getString(R.string.open_leagues));
                bundle.putInt(LeagueDetailFragment.KEY_LEAGUE_ID, details.getId());
                AloneFragmentActivity.with(this)
                        .parameters(bundle)
                        .start(LeagueDetailFragment.class);
            }, null, null, join -> {

            });
            rvRecyclerView.init(mActivity, leaguesAdapter)
                    .setOnExtLoadMoreListener(() -> {
                        page++;
                        presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
                    })
                    .setOnExtRefreshListener(() -> Optional.from(rvRecyclerView).doIfPresent(rv -> {
                        page = 1;
                        rv.clearItems();
                        presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
                    }));

            // load data
            presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    void onClickFilter() {


        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            orderBy = orderBy.equalsIgnoreCase("desc") ? "asc" : "desc";
            YoYo.with(orderBy.equalsIgnoreCase("desc") ? Techniques.RotateInUpLeft : Techniques.RotateInDownRight).playOn(svSearchView.getFilter());
            rv.clearItems();
            page = 1;
            presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        });
    }

    void onPerformSearch(String q) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            query = q;
            rv.clearItems();
            page = 1;
            presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        });
    }

    @NonNull
    @Override
    public IOpenLeaguePresenter<IOpenLeagueView> createPresenter() {
        return new OpenLeagueDataPresenter(getAppComponent());
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
