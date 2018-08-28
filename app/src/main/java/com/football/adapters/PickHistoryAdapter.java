package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PickHistoryResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PickHistoryAdapter extends DefaultAdapter<PickHistoryResponse> {

    public PickHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.pick_history_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new PickHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PickHistoryResponse data, int position) {
        PickHolder holder = (PickHolder) defaultHolder;

        if (position == 0 || !getItem(position - 1).getRound().equals(data.getRound())) {
            holder.tvRoundNumber.setVisibility(View.VISIBLE);
            holder.tvRoundNumber.setText(mContext.getString(R.string.round_number, data.getRound()));
        } else {
            holder.tvRoundNumber.setVisibility(View.GONE);
        }
        holder.tvTeam.setText(data.getTeam().getName());
        holder.tvClub.setText(AppUtilities.getNameOrMe(holder.itemView.getContext(), data.getTeam()));
        holder.tvPlayerName.setText(data.getPlayer().getName());
        ImageLoaderUtils.displayImage(data.getPlayer().getPhoto(), holder.ivAvatar);

    }

    public static class PickHolder extends DefaultHolder {

        @BindView(R.id.tvRoundNumber)
        ExtTextView tvRoundNumber;
        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvClub)
        ExtTextView tvClub;
        @BindView(R.id.tvPlayerName)
        ExtTextView tvPlayerName;

        public PickHolder(View itemView) {
            super(itemView);
        }
    }
}
