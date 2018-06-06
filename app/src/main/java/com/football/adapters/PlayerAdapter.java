package com.football.adapters;

import android.content.Context;
import android.view.View;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class PlayerAdapter extends ExtBaseAdapter<PlayerResponse, PlayerAdapter.ViewHolder> {
    private Consumer<PlayerResponse> responseConsumer;

    public PlayerAdapter(Context context, List<PlayerResponse> newsResponses, Consumer<PlayerResponse> responseConsumer) {
        super(context, newsResponses);
        this.responseConsumer = responseConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.player_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, PlayerResponse data) {

    }

    static class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.tvDay)
        ExtTextView tvDay;
        @BindView(R.id.tvMonth)
        ExtTextView tvMonth;
        @BindView(R.id.tvContent)
        ExtTextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
