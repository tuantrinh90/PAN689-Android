package com.football.adapters;

import android.content.Context;
import android.view.View;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.util.StringUtils;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class TeamSquadAdapter extends ExtBaseAdapter<PlayerResponse, TeamSquadAdapter.ViewHolder> {
    /**
     * @param ctx
     * @param its
     */
    public TeamSquadAdapter(Context ctx, List<PlayerResponse> its) {
        super(ctx, its);
    }

    @Override
    protected int getViewId() {
        return R.layout.team_squad_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder viewHolder, PlayerResponse data) {
        ImageLoaderUtils.displayImage(data.getPhoto(), viewHolder.ivAvatar);
        viewHolder.tvName.setText(data.getName());

        viewHolder.tvPositionPrimary.setBackground(data.getPositionBackground(context, data.getMainPosition()));
        viewHolder.tvPositionPrimary.setText(data.getMainPositionText());
        viewHolder.tvPositionPrimary.setVisibility(StringUtils.isEmpty(data.getMainPositionText()) ? View.GONE : View.VISIBLE);

        viewHolder.tvPositionSecond.setBackground(data.getPositionBackground(context, data.getMinorPosition()));
        viewHolder.tvPositionSecond.setText(data.getMinorPositionText());
        viewHolder.tvPositionSecond.setVisibility(StringUtils.isEmpty(data.getMinorPositionText()) ? View.GONE: View.VISIBLE);

        viewHolder.tvInjured.setText(data.getInjuredText(context));
        viewHolder.tvInjured.setVisibility(data.getInjured() ? View.VISIBLE : View.GONE);
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvPositionPrimary) ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond) ExtTextView tvPositionSecond;
        @BindView(R.id.tvInjured) ExtTextView tvInjured;
        @BindView(R.id.tvValue) ExtTextView tvValue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
