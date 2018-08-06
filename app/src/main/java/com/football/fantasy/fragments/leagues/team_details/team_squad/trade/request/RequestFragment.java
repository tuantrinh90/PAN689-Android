package com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.TradeAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TradeResponse;

import java.util.List;

import butterknife.BindView;

public class RequestFragment extends BaseMvpFragment<IRequestView, IRequestPresenter<IRequestView>> implements IRequestView {

    public static final int REQUEST_FROM = 0;
    public static final int REQUEST_TO = 1;

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
                team -> {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(team.getId(), league))
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

    private void getTradeRequests() {
        presenter.getTradeRequests(type, league.getId(), teamId, page);
    }

    private void refreshData() {
        page = 1;
        rvRequest.clear();
        getTradeRequests();
    }

    @Override
    public void displayTrades(List<TradeResponse> trades) {
        rvRequest.addItems(trades);
    }
}
