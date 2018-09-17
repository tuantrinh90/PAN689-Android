package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TradeResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TradeReviewingAdapter extends DefaultAdapter<TradeResponse> {

    public TradeReviewingAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.league_detail_trade_reviewing_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new TradeReviewingHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TradeResponse data, int position) {
        TradeReviewingHolder holder = (TradeReviewingHolder) defaultHolder;

        ImageLoaderUtils.displayImage(data.getTeam().getLogo(), holder.ivAvatarTeam1);
        holder.tvTitleTeam1.setText(data.getTeam().getName());

        ImageLoaderUtils.displayImage(data.getWithTeam().getLogo(), holder.ivAvatarTeam2);
        holder.tvTitleTeam2.setText(data.getWithTeam().getName());

        holder.tvTime.setText(AppUtilities.getTime(data.getDeadline(), Constant.FORMAT_DATE_TIME_SERVER, Constant.FORMAT_DATE_TIME));
        holder.tvPlayers.setText(mContext.getString(data.getTotalPlayer() > 1 ? R.string.total_players : R.string.total_player, data.getTotalPlayer()));

        holder.itemView.setOnClickListener(v -> {
            TradeResponse trade = getItem(defaultHolder.getAdapterPosition());
//            itemCallback.accept(trade);
        });
        holder.ivAvatarTeam1.setOnClickListener(v -> {
            TradeResponse trade = getItem(defaultHolder.getAdapterPosition());
//            teamDetailCallback.accept(trade.getTeam());
        });
        holder.ivAvatarTeam2.setOnClickListener(v -> {
            TradeResponse trade = getItem(defaultHolder.getAdapterPosition());
//            teamDetailCallback.accept(trade.getWithTeam());
        });
    }

    static class TradeReviewingHolder extends DefaultHolder {

        @BindView(R.id.ivAvatarTeam1)
        CircleImageView ivAvatarTeam1;
        @BindView(R.id.tvTime)
        ExtTextView tvTime;
        @BindView(R.id.tvPlayers)
        ExtTextView tvPlayers;
        @BindView(R.id.ivAvatarTeam2)
        CircleImageView ivAvatarTeam2;
        @BindView(R.id.tvTitleTeam1)
        ExtTextView tvTitleTeam1;
        @BindView(R.id.tvTitleTeam2)
        ExtTextView tvTitleTeam2;

        public TradeReviewingHolder(View itemView) {
            super(itemView);
        }
    }
}