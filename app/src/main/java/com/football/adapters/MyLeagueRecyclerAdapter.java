package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.GeneralUtils;
import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class MyLeagueRecyclerAdapter extends BaseRecyclerViewAdapter<LeagueResponse, MyLeagueRecyclerAdapter.ViewHolder> {
    Consumer<LeagueResponse> leagueConsumer;

    public MyLeagueRecyclerAdapter(Context context, List<LeagueResponse> leagueResponses, Consumer<LeagueResponse> leagueConsumer) {
        super(context, leagueResponses);
        this.leagueConsumer = leagueConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.my_league_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeagueResponse league = getItem(position);
        assert league != null;

        holder.ivAvatar.setImageUri(league.getLogo());
        holder.tvTitle.setText(league.getName());
        holder.tvDescription.setText(holder.itemView.getContext().getString(R.string.me));
        holder.tvRankNumber.setText(String.valueOf(league.getRank()));
        holder.tvRankTotal.setText(String.valueOf(league.getNumberOfUser()));
        holder.ivArrowUp.setImageResource(league.getRankStatus() > 0 ? R.drawable.ic_arrow_upward_green
                : (league.getRankStatus() < 0 ? R.drawable.ic_arrow_upward_green_down : 0));
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(leagueConsumer).doIfPresent(c -> c.accept(league)));

        // update layout
        DisplayMetrics displayMetrics = GeneralUtils.getDisplayMetrics(context);
        int marginLayout = GeneralUtils.convertDpMeasureToPixel(context, R.dimen.padding_layout);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(displayMetrics.widthPixels - marginLayout * 2, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(position == 0 ? marginLayout : marginLayout / 3, 0, (position == getItemCount() - 1) ? marginLayout : marginLayout / 3, 0);
        holder.llContainer.setLayoutParams(layoutParams);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
