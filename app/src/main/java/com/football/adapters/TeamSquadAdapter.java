package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.util.StringUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import java8.util.function.Consumer;

public class TeamSquadAdapter extends DefaultAdapter<PlayerResponse> {

    private final Consumer<PlayerResponse> clickCallback;
    private Consumer<PlayerResponse> addCallback;

    public TeamSquadAdapter(Context context, Consumer<PlayerResponse> clickCallback) {
        super(context);
        this.clickCallback = clickCallback;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_squad_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new TeamSquadHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PlayerResponse data, int position) {
        TeamSquadHolder holder = (TeamSquadHolder) defaultHolder;
        Context context = holder.itemView.getContext();

        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);
        holder.tvName.setText(data.getName());
        holder.tvValue.setText(String.valueOf(data.getPoint()));

        holder.tvPositionPrimary.setBackground(data.getPositionBackground(context, data.getMainPosition()));
        holder.tvPositionPrimary.setText(data.getMainPositionText());
        holder.tvPositionPrimary.setVisibility(StringUtils.isEmpty(data.getMainPositionText()) ? View.GONE : View.VISIBLE);

        holder.tvPositionSecond.setBackground(data.getPositionBackground(context, data.getMinorPosition()));
        holder.tvPositionSecond.setText(data.getMinorPositionText());
        holder.tvPositionSecond.setVisibility(StringUtils.isEmpty(data.getMinorPositionText()) ? View.GONE : View.VISIBLE);

        holder.tvInjured.setText(data.getInjuredText(context));
        holder.tvInjured.setVisibility(data.getInjured() ? View.VISIBLE : View.GONE);

        boolean b = !data.isTrading() && addCallback != null;
        holder.ivAdd.setVisibility(addCallback != null ? View.VISIBLE : View.GONE);
        holder.ivAdd.setEnabled(b);
        holder.ivAdd.setImageResource(b ? R.drawable.ic_add_white : R.drawable.ic_check_green_24_px);
        holder.ivAdd.setBackgroundResource(b ? R.drawable.bg_circle_yellow : 0);

        holder.itemView.setOnClickListener(v -> clickCallback.accept(getItem(defaultHolder.getAdapterPosition())));
        holder.ivAdd.setOnClickListener(v -> addCallback.accept(getItem(defaultHolder.getAdapterPosition())));
    }

    public void setAddCallback(Consumer<PlayerResponse> addCallback) {
        this.addCallback = addCallback;
    }

    static class TeamSquadHolder extends DefaultHolder {
        @BindView(R.id.ivAvatar)
        CircleImageView ivAvatar;
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvPositionPrimary)
        ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond)
        ExtTextView tvPositionSecond;
        @BindView(R.id.tvInjured)
        ExtTextView tvInjured;
        @BindView(R.id.tvValue)
        ExtTextView tvValue;
        @BindView(R.id.ivAdd)
        ImageView ivAdd;

        public TeamSquadHolder(View itemView) {
            super(itemView);
        }
    }
}
