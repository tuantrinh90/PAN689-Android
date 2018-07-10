package com.football.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TransferHistoryResponse;

import java.util.List;

public class RecordAdapter extends DefaultAdapter<TransferHistoryResponse> {

    public RecordAdapter() {
        super();
    }
    public RecordAdapter(List<TransferHistoryResponse> dataSet) {
        super(dataSet);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_squad_trade_record_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new RecordHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TransferHistoryResponse data, int position) {

    }

    static class RecordHolder extends DefaultHolder {

        public RecordHolder(View itemView) {
            super(itemView);
        }
    }
}
