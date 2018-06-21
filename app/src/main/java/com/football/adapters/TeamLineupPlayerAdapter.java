package com.football.adapters;

import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;

import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamLineupPlayerAdapter extends DefaultAdapter<PlayerResponse> {


    public TeamLineupPlayerAdapter(List<PlayerResponse> dataSet) {
        super(dataSet);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_lineup_player_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new PlayerHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PlayerResponse data, int position) {
        PlayerHolder holder = (PlayerHolder) defaultHolder;
        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);
        AppUtilities.displayPlayerPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        AppUtilities.displayPlayerPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());
        holder.tvName.setText(data.getName());
    }

    static class PlayerHolder extends DefaultHolder {

        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvPositionPrimary)
        ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond)
        ExtTextView tvPositionSecond;
        @BindView(R.id.tvName)
        ExtTextView tvName;

        public PlayerHolder(View itemView) {
            super(itemView);
        }
    }
}
