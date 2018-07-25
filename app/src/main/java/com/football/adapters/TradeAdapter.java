package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TradeResponse;

public class TradeAdapter extends DefaultAdapter<TradeResponse> {

    public TradeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.league_detail_trade_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new TradeHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TradeResponse data, int position) {

    }

    static class TradeHolder extends DefaultHolder {

        public TradeHolder(View itemView) {
            super(itemView);
        }
    }
}
