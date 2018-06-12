package com.football.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerPoolItemAdapter extends ExtBaseAdapter<PlayerResponse, PlayerPoolItemAdapter.ViewHolder> {
    /**
     * @param ctx
     * @param its
     */
    public PlayerPoolItemAdapter(Context ctx, List<PlayerResponse> its) {
        super(ctx, its);
    }

    @Override
    protected int getViewId() {
        return R.layout.player_pool_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, PlayerResponse data) {
//        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);
        holder.tvName.setText(data.getName());
        holder.tvClub.setText(data.getRealClub().getName());
        holder.tvValue.setText(holder.itemView.getContext().getString(R.string.money_prefix, data.getTransferValueDisplay()));
        setPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        setPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());
        boolean checked = data.getSelected();
        holder.ivAdd.setImageResource(checked ? R.drawable.ic_tick : R.drawable.ic_add_white_small);
        holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);
    }

    private void setPosition(TextView textView, Integer position, String value) {
        if (TextUtils.isEmpty(value)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(value);
            switch (position) {
                case PlayerResponse.POSITION_ATTACKER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_a);
                    break;

                case PlayerResponse.POSITION_MIDFIELDER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_m);
                    break;

                case PlayerResponse.POSITION_DEFENDER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_d);
                    break;

                case PlayerResponse.POSITION_GOALKEEPER:
                    textView.setBackgroundResource(R.drawable.bg_player_position_g);
                    break;
            }
        }
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvClub)
        ExtTextView tvClub;
        @BindView(R.id.tvPositionPrimary)
        ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond)
        ExtTextView tvPositionSecond;
        @BindView(R.id.tvValue)
        ExtTextView tvValue;
        @BindView(R.id.tvPoint)
        ExtTextView tvPoint;
        @BindView(R.id.ivChange)
        ImageView ivChange;
        @BindView(R.id.tvStatOne)
        ExtTextView tvStatOne;
        @BindView(R.id.ivAdd)
        ImageView ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
