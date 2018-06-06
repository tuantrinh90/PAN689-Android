package com.football.fantasy.fragments.leagues.your_team.player_list;

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
import com.football.adapters.PlayerAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.searchs.SearchView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;

public class PlayerListFragment extends BaseMainMvpFragment<IPlayerListView, IPlayerListPresenter<IPlayerListView>> implements IPlayerListView {
    private static final String TAG = "PlayerListFragment";

    public static PlayerListFragment newInstance() {
        return new PlayerListFragment();
    }

    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    SearchView svSearchView;
    List<PlayerResponse> playerResponses;
    PlayerAdapter playerAdapter;

    String orderBy = "desc";
    int page = 1;
    String query = "";

    @Override
    public int getResourceId() {
        return R.layout.player_list_fragment;
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
            AbsListView.LayoutParams layoutParams = new AbsListView.LayoutParams(displayMetrics.widthPixels - paddingLayout * 2,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            svSearchView.setPadding(marginLayout, 0, marginLayout, marginLayout);
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_black_24_px);

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

            // playerResponses
            playerAdapter = new PlayerAdapter(mActivity, playerResponses, details -> {

            });
            rvRecyclerView.init(mActivity, playerAdapter)
                    .setOnExtLoadMoreListener(() -> {
                        page++;
//                        presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
                    })
                    .setOnExtRefreshListener(() -> Optional.from(rvRecyclerView).doIfPresent(rv -> {
                        page = 1;
                        rv.clearItems();
//                        presenter.getOpenLeagues(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
                    }));

            // load data
            presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        } catch (Resources.NotFoundException e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public IPlayerListPresenter<IPlayerListView> createPresenter() {
        return new PlayerListPresenter(getAppComponent());
    }

    void onClickFilter() {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            orderBy = orderBy.equalsIgnoreCase("desc") ? "asc" : "desc";
            svSearchView.getFilter().animate().rotation(orderBy.equalsIgnoreCase("desc") ? 0 : 180)
                    .setDuration(500).setInterpolator(new LinearInterpolator()).start();
            rv.clearItems();
            page = 1;
            presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        });
    }

    void onPerformSearch(String q) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            query = q;
            rv.clearItems();
            page = 1;
            presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query);
        });
    }

    @Override
    public void notifyDataSetChangedPlayers(List<PlayerResponse> data) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(data));
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
