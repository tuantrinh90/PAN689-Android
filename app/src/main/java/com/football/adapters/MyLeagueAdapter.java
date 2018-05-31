package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class MyLeagueAdapter extends ExtBaseAdapter<LeagueResponse, MyLeagueAdapter.ViewHolder> {
    Consumer<LeagueResponse> leagueConsumer;

    public MyLeagueAdapter(Context context, List<LeagueResponse> leagueResponses, Consumer<LeagueResponse> leagueConsumer) {
        super(context, leagueResponses);
        this.leagueConsumer = leagueConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.my_league_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, LeagueResponse leagueResponse) {
        holder.ivAvatar.setImageUri(leagueResponse.getLogo());
        holder.tvTitle.setText(leagueResponse.getName());
        holder.tvDescription.setText(leagueResponse.getUser().getName());
        holder.tvRankNumber.setText(String.valueOf(leagueResponse.getCurrentNumberOfUser()));
        holder.tvRankTotal.setText(String.valueOf(leagueResponse.getNumberOfUser()));
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(leagueConsumer).doIfPresent(c -> c.accept(leagueResponse)));
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.llContainer)
        LinearLayout llContainer;
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTitle)
        ExtTextView tvTitle;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;
        @BindView(R.id.tvRankNumber)
        ExtTextView tvRankNumber;
        @BindView(R.id.tvRankTotal)
        ExtTextView tvRankTotal;
        @BindView(R.id.ivArrowUp)
        ImageView ivArrowUp;
        @BindView(R.id.ivArrowDown)
        ImageView ivArrowDown;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
