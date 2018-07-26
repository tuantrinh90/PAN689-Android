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
import com.football.models.responses.TradeResponse;
import com.football.utilities.AppUtilities;
import com.football.utilities.Constant;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TradeAdapter extends DefaultAdapter<TradeResponse> {

    public TradeAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_squad_trade_request_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new TradeHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TradeResponse data, int position) {
        TradeHolder holder = (TradeHolder) defaultHolder;

        ImageLoaderUtils.displayImage(data.getTeam().getLogo(), holder.ivAvatarTeam1);
        holder.tvTitleTeam1.setText(data.getTeam().getName());

        ImageLoaderUtils.displayImage(data.getWithTeam().getLogo(), holder.ivAvatarTeam2);
        holder.tvTitleTeam2.setText(data.getWithTeam().getName());

        holder.tvTime.setText(AppUtilities.getTime(data.getDeadline(), Constant.FORMAT_DATE_TIME_SERVER, Constant.FORMAT_DATE_TIME));

    }

    static class TradeHolder extends DefaultHolder {

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

        public TradeHolder(View itemView) {
            super(itemView);
        }
    }
}
