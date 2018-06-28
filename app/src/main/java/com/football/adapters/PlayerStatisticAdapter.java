package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerRoundPointResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerStatisticAdapter extends ExtBaseAdapter<PlayerRoundPointResponse, PlayerStatisticAdapter.ViewHolder> {

    public PlayerStatisticAdapter(Context ctx, List<PlayerRoundPointResponse> its) {
        super(ctx, its);
    }

    @Override
    protected int getViewId() {
        return R.layout.player_statistic_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, PlayerRoundPointResponse data) {
        holder.tvRound.setText(String.valueOf(data.getRound()));
        holder.tvPoint.setText(String.valueOf(data.getPoint()));

        int change = data.getChange();
        holder.tvChange.setText(change == 0 ? "-" : String.valueOf(Math.abs(change)));

        if (change > 0) {
            holder.ivChange.setImageResource(R.drawable.ic_arrow_upward_white_small);
            holder.ivChange.setBackgroundResource(R.drawable.bg_circle_green);
            holder.ivChange.setVisibility(View.VISIBLE);
        } else if (change < 0) {
            holder.ivChange.setImageResource(R.drawable.ic_arrow_downward_white_small);
            holder.ivChange.setBackgroundResource(R.drawable.bg_circle_red);
            holder.ivChange.setVisibility(View.VISIBLE);
        } else {
            holder.ivChange.setVisibility(View.GONE);
        }
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {

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
