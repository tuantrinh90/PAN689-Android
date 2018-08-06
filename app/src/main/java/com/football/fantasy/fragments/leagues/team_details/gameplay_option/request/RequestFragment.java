package com.football.fantasy.fragments.leagues.team_details.gameplay_option.request;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.TradeAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.TradeResponse;

import java.util.List;

import butterknife.BindView;

public class RequestFragment extends BaseMvpFragment<IRequestView, IRequestPresenter<IRequestView>> implements IRequestView {

    public static final int REQUEST_FROM = 0;
    public static final int REQUEST_TO = 1;

    private static final String KEY_TYPE = "TYPE";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    private static final String KEY_TEAM_ID = "TEAM_ID";

    @BindView(R.id.rvRequest)
    ExtRecyclerView<TradeResponse> rvRequest;

    private int type;
    private int leagueId;
    private int teamId;

    private int page = 1;

    public static RequestFragment newInstance(int type, int leagueId, int teamId) {
        RequestFragment fragment = new RequestFragment();

        Bundle bundle = new Bundle();
        bundle.putInt(KEY_TYPE, type);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
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
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
        teamId = getArguments().getInt(KEY_TEAM_ID);
    }

    @NonNull
    @Override
    public IRequestPresenter<IRequestView> createPresenter() {
        return new RequestDataPresenter(getAppComponent());
    }

    void initRecyclerView() {
        TradeAdapter adapter = new TradeAdapter(getContext());
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
        presenter.getTradeRequests(type, leagueId, teamId, page);
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
