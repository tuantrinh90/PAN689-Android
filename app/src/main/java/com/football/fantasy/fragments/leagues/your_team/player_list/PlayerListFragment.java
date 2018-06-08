package com.football.fantasy.fragments.leagues.your_team.player_list;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
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

    @BindView(R.id.svSearch)
    SearchView svSearchView;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<PlayerResponse> playerResponses;
    PlayerAdapter playerAdapter;

    String orderBy = "desc";
    int page = 1;
    String query = "";
    String mainPosition = "";

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
            svSearchView.getFilter().setImageResource(R.drawable.ic_filter_list_black_24_px);

            // update hint
            svSearchView.getSearchView().setHint(R.string.search_public_players);

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
                        presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
                    })
                    .setOnExtRefreshListener(() -> Optional.from(rvRecyclerView).doIfPresent(rv -> {
                        page = 1;
                        rv.clearItems();
                        presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
                    }));

            // load data
            presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
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


        });
    }

    void onPerformSearch(String q) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            query = q;
            rv.clearItems();
            page = 1;
            presenter.getPlayers(orderBy, page, ExtPagingListView.NUMBER_PER_PAGE, query, mainPosition);
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
