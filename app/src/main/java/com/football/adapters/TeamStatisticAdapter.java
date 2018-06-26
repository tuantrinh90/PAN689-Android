package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;
import com.football.models.responses.RoundResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TeamStatisticAdapter extends ExtBaseAdapter<RoundResponse, TeamStatisticAdapter.ViewHolder> {
    /**
     * @param ctx
     * @param its
     */
    public TeamStatisticAdapter(Context ctx, List<RoundResponse> its) {
        super(ctx, its);
    }

    @Override
    protected int getViewId() {
        return R.layout.team_statistic_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, RoundResponse data) {
        viewHolder.tvRound.setText(String.valueOf(data.getRound()));
        viewHolder.tvPoint.setText(String.valueOf(data.getPoint()));
        viewHolder.tvChange.setText(String.valueOf(data.getChange()));
        viewHolder.ivChange.setBackgroundResource(data.isIncrease() ? R.drawable.bg_circle_green : R.drawable.bg_circle_red);
        viewHolder.ivChange.setImageResource(data.isIncrease() ? R.drawable.ic_arrow_upward_white_small : R.drawable.ic_arrow_downward_white_small);
    }

    static class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.tvRound)
        ExtTextView tvRound;
        @BindView(R.id.tvPoint)
        ExtTextView tvPoint;
        @BindView(R.id.tvChange)
        ExtTextView tvChange;
        @BindView(R.id.ivChange)
        ImageView ivChange;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
