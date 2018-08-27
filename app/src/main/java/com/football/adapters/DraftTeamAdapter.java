package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.function.Consumer;

public class DraftTeamAdapter extends DefaultAdapter<TeamResponse> {

    private final Consumer<TeamResponse> detailCallback;
    private final Consumer<TeamResponse> lineupCallback;

    public DraftTeamAdapter(Context context, Consumer<TeamResponse> detailCallback, Consumer<TeamResponse> lineupCallback) {
        super(context);
        this.detailCallback = detailCallback;
        this.lineupCallback = lineupCallback;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.draft_teams_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new DraftTeamHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TeamResponse data, int position) {
        DraftTeamHolder holder = (DraftTeamHolder) defaultHolder;

        ImageLoaderUtils.displayImage(data.getLogo(), holder.ivAvatar);
        holder.tvTeam.setText(data.getName());
        holder.tvOwner.setText(AppUtilities.getNameOrMe(holder.itemView.getContext(), data));
        holder.textOrder.setText(mContext.getString(R.string.number_order, position + 1));

        holder.itemView.setOnClickListener(v -> {
            detailCallback.accept(data);
        });

        holder.textLineup.setOnClickListener(v -> {
            lineupCallback.accept(data);
        });
    }

    public static class DraftTeamHolder extends DefaultHolder {

        @BindView(R.id.text_order)
        ExtTextView textOrder;
        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvOwner)
        ExtTextView tvOwner;
        @BindView(R.id.text_lineup)
        ExtTextView textLineup;

        public DraftTeamHolder(View itemView) {
            super(itemView);
        }
    }
}
