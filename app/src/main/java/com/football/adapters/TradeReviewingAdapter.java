package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TradeResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.function.Consumer;

public class TradeReviewingAdapter extends DefaultAdapter<TradeResponse> {

    private final String type;
    private final Consumer<TradeResponse> itemCallback;

    public TradeReviewingAdapter(Context context, String type, Consumer<TradeResponse> itemCallback) {
        super(context);
        this.type = type;
        this.itemCallback = itemCallback;
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

        holder.tvTime.setText(AppUtilities.getTimeFormatted(data.getDeadline()));
        holder.tvPlayers.setText(mContext.getString(data.getTotalPlayer() > 1 ? R.string.total_players : R.string.total_player, data.getTotalPlayer()));

        // gone all views
        holder.tvDeadline.setVisibility(View.GONE);
        holder.tvDescription.setVisibility(View.GONE);
        holder.imageIcon.setVisibility(View.GONE);
        holder.tvStatus.setVisibility(View.GONE);

        if (type.equals(TradeResponse.TYPE_REVIEWING)) {
            displayReviewingStatus(holder, data);
        } else {
            displayReviewedStatus(holder, data);
        }
        holder.itemView.setOnClickListener(v -> itemCallback.accept(data));
    }

    private void displayReviewingStatus(TradeReviewingHolder holder, TradeResponse data) {
        if (data.getStatus() == TradeResponse.STATUS_ACCEPT) {
            holder.imageIcon.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);

            holder.imageIcon.setImageResource(R.drawable.ic_check_green_24_px);
            holder.tvStatus.setText(mContext.getString(R.string.approved_));
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_green));
            holder.bottom.setBackgroundResource(0);

        } else if (data.getStatus() == TradeResponse.STATUS_REJECT) {
            holder.imageIcon.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);

            holder.imageIcon.setImageResource(R.drawable.ic_close_red_24_px);
            holder.tvStatus.setText(mContext.getString(R.string.rejected_));
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_red));
            holder.bottom.setBackgroundResource(0);

        } else {
            holder.tvDeadline.setVisibility(View.VISIBLE);
            holder.tvDescription.setVisibility(View.VISIBLE);

            holder.tvDescription.setText(AppUtilities.getTimeFormatted(data.getReviewDeadline()));
            holder.tvDescription.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
            holder.bottom.setBackgroundResource(R.drawable.bg_blue_gradient_radius_bottom);
        }
    }

    private void displayReviewedStatus(TradeReviewingHolder holder, TradeResponse data) {
        if (data.getStatus() == TradeResponse.STATUS_SUCCESSFUL) {
            holder.imageIcon.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);

            holder.imageIcon.setImageResource(R.drawable.ic_check_white_24_px);
            holder.tvStatus.setText(mContext.getString(R.string.successful));
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
            holder.bottom.setBackgroundResource(R.drawable.bg_green_gradient_radius_bottom);

        } else if (data.getStatus() == TradeResponse.STATUS_FAILED) {
            holder.imageIcon.setVisibility(View.VISIBLE);
            holder.tvStatus.setVisibility(View.VISIBLE);

            holder.imageIcon.setImageResource(R.drawable.ic_close_white_24_px);
            holder.tvStatus.setText(mContext.getString(R.string.failed));
            holder.tvStatus.setTextColor(ContextCompat.getColor(mContext, R.color.color_white));
            holder.bottom.setBackgroundResource(R.drawable.bg_red_gradient_radius_bottom);
        }
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
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;
        @BindView(R.id.tvDeadline)
        ExtTextView tvDeadline;
        @BindView(R.id.tvStatus)
        ExtTextView tvStatus;
        @BindView(R.id.image_icon)
        ImageView imageIcon;
        @BindView(R.id.llBottomStatus)
        View bottom;

        public TradeReviewingHolder(View itemView) {
            super(itemView);
        }
    }
}
