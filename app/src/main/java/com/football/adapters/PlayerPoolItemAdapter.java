package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;

public class PlayerPoolItemAdapter extends ExtBaseAdapter<PlayerResponse, PlayerPoolItemAdapter.ViewHolder> {
    /**
     * @param ctx
     * @param its
     */
    public PlayerPoolItemAdapter(Context ctx, List<PlayerResponse> its) {
        super(ctx, its);
    }

    @Override
    protected int getViewId() {
        return R.layout.player_pool_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, PlayerResponse data) {

    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvClub)
        ExtTextView tvClub;
        @BindView(R.id.tvPositionPrimary)
        ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond)
        ExtTextView tvPositionSecond;
        @BindView(R.id.tvValue)
        ExtTextView tvValue;
        @BindView(R.id.tvPoint)
        ExtTextView tvPoint;
        @BindView(R.id.ivChange)
        ImageView ivChange;
        @BindView(R.id.tvStatOne)
        ExtTextView tvStatOne;
        @BindView(R.id.ivAdd)
        ImageView ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
