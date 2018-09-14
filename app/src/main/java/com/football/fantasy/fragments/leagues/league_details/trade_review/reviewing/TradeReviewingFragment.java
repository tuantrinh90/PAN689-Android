package com.football.fantasy.fragments.leagues.league_details.trade_review.reviewing;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.TradeReviewingAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TradeResponse;

import java.util.List;

import butterknife.BindView;

// Get trade review results
public class TradeReviewingFragment extends BaseMvpFragment<ITradeReviewingView, ITradeReviewingPresenter<ITradeReviewingView>> implements ITradeReviewingView {

    private static final String KEY_LEAGUE = "LEAGUE";


    @BindView(R.id.rv_reviews)
    ExtRecyclerView<TradeResponse> rvReviews;


    public static TradeReviewingFragment newInstance(LeagueResponse league) {
        TradeReviewingFragment fragment =  new TradeReviewingFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
//        fragment.
    }

    private LeagueResponse league;
    private int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_trade_reviewing_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        bindButterKnife(view);

        initRecyclerView();

        getTradeRequests();
    }

    private void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    @NonNull
    @Override
    public ITradeReviewingPresenter<ITradeReviewingView> createPresenter() {
        return new TradeReviewingPresenter(getAppComponent());
    }

    private void initRecyclerView() {
        TradeReviewingAdapter adapter = new TradeReviewingAdapter(getContext());
        rvReviews
                .adapter(adapter)
                .refreshListener(this::refresh)
                .loadMoreListener(() -> {
                    page++;
                    getTradeRequests();
                })
                .build();
    }

    private void getTradeRequests() {
        presenter.getReviews(league.getId(), page);
    }

    private void refresh() {
        page = 1;
        rvReviews.clear();
        rvReviews.startLoading();
        getTradeRequests();
    }

    @Override
    public void displayReviews(List<TradeResponse> list) {
        rvReviews.addItems(list);
    }

    @Override
    public void stopLoading() {
        rvReviews.stopLoading();
    }
}
