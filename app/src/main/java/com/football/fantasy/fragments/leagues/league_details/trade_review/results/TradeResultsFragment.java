package com.football.fantasy.fragments.leagues.league_details.trade_review.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;

public class TradeResultsFragment extends BaseMvpFragment<ITradeResultsView, ITradeResultsPresenter<ITradeResultsView>> implements ITradeResultsView {
    public static TradeResultsFragment newInstance() {
        return new TradeResultsFragment();
    }

    private int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_trade_results_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initRecyclerView();

        getNotifications();
    }

    @NonNull
    @Override
    public ITradeResultsPresenter<ITradeResultsView> createPresenter() {
        return new TradeResultsPresenter(getAppComponent());
    }

    private void initRecyclerView() {
    }

    private void getNotifications() {
//        presenter.getReviews(page);
    }

    private void refresh() {
        page = 1;
//        rvNotification.clear();
//        rvNotification.startLoading();
        getNotifications();
    }

    @Override
    public void stopLoading() {
    }
}
