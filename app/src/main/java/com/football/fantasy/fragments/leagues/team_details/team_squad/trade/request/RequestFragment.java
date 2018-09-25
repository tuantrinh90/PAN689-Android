package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.TradeAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.events.TradeEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.TradeRequestFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew.ProposalReviewFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TradeResponse;

import java.util.List;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

public class RequestFragment extends BaseMvpFragment<IRequestView, IRequestPresenter<IRequestView>> implements IRequestView {

    public static final int REQUEST_BY_YOU = 0;
    public static final int REQUEST_TO_YOU = 1;

    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_LEAGUE = "LEAGUE";
    private static final String KEY_TEAM_ID = "TEAM_ID";

    @BindView(R.id.rvRequest)
    ExtRecyclerView<TradeResponse> rvRequest;

    private int type;
    private LeagueResponse league;
    private int teamId;

    private int page = 1;

    public static RequestFragment newInstance(int type, LeagueResponse league, int teamId) {
        RequestFragment fragment = new RequestFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putInt(KEY_TEAM_ID, teamId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_request_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initRecyclerView();
        getTradeRequests();

        registerBus();
    }

    private void getDataFromBundle() {
        type = getArguments().getInt(KEY_TYPE);
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        teamId = getArguments().getInt(KEY_TEAM_ID);
    }

    @NonNull
    @Override
    public IRequestPresenter<IRequestView> createPresenter() {
        return new RequestDataPresenter(getAppComponent());
    }

    void initRecyclerView() {
        TradeAdapter adapter = new TradeAdapter(
                getContext(),
                trade -> {
                    ProposalReviewFragment.start(getContext(), getString(R.string.trade_request), trade, type);
                },
                team -> {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(getString(R.string.trade_request), team.getId(), league))
                            .start(TeamDetailFragment.class);
                });
        rvRequest
                .adapter(adapter)
                .refreshListener(this::refreshData)
                .loadMoreListener(() -> {
                    page++;
                    getTradeRequests();
                })
                .build();
    }

    private void registerBus() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(TradeEvent.class)
                    .subscribeWith(new DisposableObserver<TradeEvent>() {
                        @Override
                        public void onNext(TradeEvent event) {
                            refreshData();
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

    private void getTradeRequests() {
        presenter.getTradeRequests(type, league.getId(), teamId, page);
    }

    private void refreshData() {
        page = 1;
        rvRequest.clear();
        getTradeRequests();
    }

    @Override
    public void displayTradeRequestLeftDisplay(String tradeRequestLeftDisplay, int pendingTradeRequest, int currentTradeRequest, int maxTradeRequest) {
        if (getParentFragment() instanceof TradeRequestFragment) {
            ((TradeRequestFragment) getParentFragment()).displayTradeRequestLeftDisplay(tradeRequestLeftDisplay, pendingTradeRequest, currentTradeRequest, maxTradeRequest);
        }
    }

    @Override
    public void displayTrades(List<TradeResponse> trades) {
        rvRequest.addItems(trades);
    }

    @Override
    public void stopLoading() {
        rvRequest.stopLoading();
    }
}
