package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.util.DateTimeUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.models.responses.TransferHistoryResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.function.Consumer;

public class RecordAdapter extends DefaultAdapter<TransferHistoryResponse> {

    private Consumer<PlayerResponse> clickConsumer;
    private final boolean transferMode;

    public RecordAdapter(Context context,  boolean transferMode, Consumer<PlayerResponse> clickConsumer) {
        super(context);
        this.transferMode = transferMode;
        this.clickConsumer = clickConsumer;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_squad_trade_record_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new RecordHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TransferHistoryResponse data, int position) {
        RecordHolder holder = (RecordHolder) defaultHolder;

        holder.tvStatus.setText(data.getStatusDisplay());
        boolean isOut = data.getStatus() == TransferHistoryResponse.STATUS_OUT;
        if (isOut) {
            holder.spaceLeft.setVisibility(View.GONE);
            holder.spaceRight.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackgroundResource(R.drawable.bg_red_radius);
            ImageLoaderUtils.displayImage(data.getFromPlayer().getPhoto(), holder.ivAvatar);
            holder.tvName.setText(data.getFromPlayer().getName());
        } else {
            holder.spaceRight.setVisibility(View.GONE);
            holder.spaceLeft.setVisibility(View.VISIBLE);
            holder.tvStatus.setBackgroundResource(R.drawable.bg_green_radius);
            ImageLoaderUtils.displayImage(data.getFromPlayer().getPhoto(), holder.ivAvatar);
            holder.tvName.setText(data.getFromPlayer().getName());
//            ImageLoaderUtils.displayImage(data.getToPlayer().getPhoto(), holder.ivAvatar);
//            holder.tvName.setText(data.getToPlayer().getName());
        }
        holder.tvTransferFee.setText(holder.itemView.getContext().getString(R.string.money_prefix, data.getTransferFeeValue()));
        holder.tvTime.setText(AppUtilities.getDateFormatted(data.getTransferAt()));

        holder.tvLabelTransfer.setVisibility(transferMode ? View.VISIBLE : View.GONE);
        holder.tvTransferFee.setVisibility(transferMode ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> {
            TransferHistoryResponse response = getItem(defaultHolder.getAdapterPosition());
            clickConsumer.accept(response.getFromPlayer());
//            clickConsumer.accept(response.getStatus() == TransferHistoryResponse.STATUS_OUT ? response.getFromPlayer() : response.getToPlayer());
        });
    }

    static class RecordHolder extends DefaultHolder {

        @BindView(R.id.space_left)
        View spaceLeft;
        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvLabelTransfer)
        ExtTextView tvLabelTransfer;
        @BindView(R.id.tvTransferFee)
        ExtTextView tvTransferFee;
        @BindView(R.id.tvStatus)
        ExtTextView tvStatus;
        @BindView(R.id.tvTime)
        ExtTextView tvTime;
        @BindView(R.id.space_right)
        View spaceRight;

        public RecordHolder(View itemView) {
            super(itemView);
        }
    }
}
