package com.football.fantasy.fragments.leagues.team_squad.trade.record;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.football.adapters.RecordAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.RecordResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class RecordFragment extends BaseMainMvpFragment<IRecordView, IRecordPresenter<IRecordView>> implements IRecordView {

    @BindView(R.id.rvRecord)
    ExtRecyclerView<RecordResponse> rvRecord;

    private int page = 1;

    public static RecordFragment newInstance() {
        return new RecordFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_record_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initRecyclerView();
    }

    @NonNull
    @Override
    public IRecordPresenter<IRecordView> createPresenter() {
        return new RecordDataPresenter(getAppComponent());
    }

    void initRecyclerView() {
        List<RecordResponse> records = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            records.add(new RecordResponse());
        }

        RecordAdapter adapter = new RecordAdapter(records);
        rvRecord.adapter(adapter)
                .refreshListener(() -> {
                    refreshData();
                })
                .loadMoreListener(() -> {
                    page++;
                    getPlayers();
                })
                .build();
    }

    private void refreshData() {

    }

    private void getPlayers() {

    }
}
