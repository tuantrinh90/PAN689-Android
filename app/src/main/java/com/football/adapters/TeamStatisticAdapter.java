package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.RoundResponse;

import butterknife.BindView;

public class TeamStatisticAdapter extends DefaultAdapter<RoundResponse> {

    public TeamStatisticAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_statistic_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new StatisticHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, RoundResponse data, int position) {

        StatisticHolder holder = (StatisticHolder) defaultHolder;

        holder.tvRound.setText(String.format("%02d", data.getRound()));
        holder.tvPoint.setText(String.valueOf(data.getPoint()));

        if (position == 0 || data.getChange() == 0) {
            holder.ivChange.setVisibility(View.GONE);

            holder.tvChange.setText("-");
        } else if (data.getChange() < 0) {
            holder.ivChange.setVisibility(View.VISIBLE);
            holder.ivChange.setImageResource(R.drawable.ic_status_down);

            holder.tvChange.setText(String.valueOf(data.getChange()));
        } else if (data.getChange() > 0) {
            holder.ivChange.setVisibility(View.VISIBLE);
            holder.ivChange.setImageResource(R.drawable.ic_status_up);

            holder.tvChange.setText(String.valueOf(data.getChange()));
        }
    }

    static class StatisticHolder extends DefaultHolder {
        @BindView(R.id.tvRound)
        ExtTextView tvRound;
        @BindView(R.id.tvPoint)
        ExtTextView tvPoint;
        @BindView(R.id.tvChange)
        ExtTextView tvChange;
        @BindView(R.id.ivChange)
        ImageView ivChange;

        public StatisticHolder(View itemView) {
            super(itemView);
        }
    }
}
