package com.football.fantasy.fragments.leagues.league_details.trade_review.sub;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.football.adapters.TradeReviewingAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.TradeEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew.ProposalReviewFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TradeResponse;

import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

// Get trade review results
public class SubTradeReviewFragment extends BaseMvpFragment<ISubTradeReviewView, ISubTradeReviewPresenter<ISubTradeReviewView>> implements ISubTradeReviewView {

    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_LEAGUE = "LEAGUE";
    private static final String KEY_PREVIEW_TRADE_ID = "PREVIEW_TRADE_ID";


    @BindView(R.id.rv_reviews)
    ExtRecyclerView<TradeResponse> rvReviews;

    public static SubTradeReviewFragment newInstance(String type, LeagueResponse league, int previewTradeId) {
        Bundle args = new Bundle();
        args.putString(KEY_TYPE, type);
        args.putSerializable(KEY_LEAGUE, league);
        args.putInt(KEY_PREVIEW_TRADE_ID, previewTradeId);

        SubTradeReviewFragment fragment = new SubTradeReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private String type;
    private LeagueResponse league;
    private int previewTradeId;

    private int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_trade_reviewing_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        setChildFragment(true);
        super.onViewCreated(view, savedInstanceState);
        getDataFromBundle();
        bindButterKnife(view);

        initRecyclerView();

        getTradeRequests();

        registerBus();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    private void getDataFromBundle() {
        type = getArguments().getString(KEY_TYPE);
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        previewTradeId = getArguments().getInt(KEY_PREVIEW_TRADE_ID);
    }

    @NonNull
    @Override
    public ISubTradeReviewPresenter<ISubTradeReviewView> createPresenter() {
        return new SubTradeReviewPresenter(getAppComponent());
    }

    private void initRecyclerView() {
        TradeReviewingAdapter adapter = new TradeReviewingAdapter(
                getContext(),
                type,
                trade -> {
                    ProposalReviewFragment.start(getContext(), getString(R.string.trade_request), trade, type);
                });
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
        presenter.getReviews(league.getId(), page, type);
    }

    private void refresh() {
        page = 1;
        rvReviews.clear();
        rvReviews.startLoading();
        getTradeRequests();
    }

    private void registerBus() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(TradeEvent.class)
                    .subscribeWith(new DisposableObserver<TradeEvent>() {
                        @Override
                        public void onNext(TradeEvent event) {
                            refresh();
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
