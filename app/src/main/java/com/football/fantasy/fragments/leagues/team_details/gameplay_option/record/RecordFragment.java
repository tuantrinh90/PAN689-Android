package com.football.fantasy.fragments.leagues.team_details.gameplay_option.record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.RecordAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.player_details.PlayerDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.football.models.responses.TransferHistoryResponse;

import java.util.List;

import butterknife.BindView;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class RecordFragment extends BaseMvpFragment<IRecordView, IRecordPresenter<IRecordView>> implements IRecordView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.rvRecord)
    ExtRecyclerView<TransferHistoryResponse> rvRecord;

    private TeamResponse team;
    private LeagueResponse league;

    private int page = 1;

    public static RecordFragment newInstance(TeamResponse team, LeagueResponse league) {
        RecordFragment fragment = new RecordFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putSerializable(KEY_LEAGUE, league);

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
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
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
                    PlayerDetailFragment.start(this,
                            player.getId(),
                            -1,
                            getString(R.string.record),
                            league.getGameplayOption());
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
        boolean isTransfer = league.getGameplayOption().equals(GAMEPLAY_OPTION_TRANSFER);
        presenter.getTransferHistories(team.getId(), isTransfer ? "transfer" : "draft", page);
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
