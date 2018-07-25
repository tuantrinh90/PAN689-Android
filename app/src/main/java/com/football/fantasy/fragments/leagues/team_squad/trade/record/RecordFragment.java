package com.football.fantasy.fragments.leagues.team_squad.trade.record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.RecordAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TransferHistoryResponse;

import java.util.List;

import butterknife.BindView;

public class RecordFragment extends BaseMainMvpFragment<IRecordView, IRecordPresenter<IRecordView>> implements IRecordView {

    private static final String KEY_TEAM = "TEAM";

    @BindView(R.id.rvRecord)
    ExtRecyclerView<TransferHistoryResponse> rvRecord;

    private TeamResponse team;

    private int page = 1;

    public static RecordFragment newInstance(TeamResponse team) {
        RecordFragment fragment = new RecordFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_record_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initRecyclerView();
        getTransferHistories();
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
    }

    @NonNull
    @Override
    public IRecordPresenter<IRecordView> createPresenter() {
        return new RecordDataPresenter(getAppComponent());
    }

    void initRecyclerView() {
        RecordAdapter adapter = new RecordAdapter(
                getContext(),
                player -> {
                    PlayerDetailFragment.start(this, player, getString(R.string.record), false);
                });
        rvRecord.adapter(adapter)
                .refreshListener(this::refreshData)
                .loadMoreListener(() -> {
                    page++;
                    getTransferHistories();
                })
                .build();
    }

    private void refreshData() {
        page = 1;
        rvRecord.clear();
        getTransferHistories();
    }

    private void getTransferHistories() {
        presenter.getTransferHistories(team.getId(), "transfer", page);
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvRecord.stopLoading();
        }
    }

    @Override
    public void displayHistories(List<TransferHistoryResponse> histories) {
        rvRecord.addItems(histories);
    }
}
