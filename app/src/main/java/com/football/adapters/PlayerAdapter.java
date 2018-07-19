package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.BiConsumer;
import java8.util.function.Consumer;

public class PlayerAdapter extends DefaultAdapter<PlayerResponse> {
    private static final String TAG = "PlayerAdapter";

    private final CompositeDisposable mDisposable = new CompositeDisposable();

    private int visibleAddButton = View.VISIBLE;

    private final Consumer<PlayerResponse> clickCallback;
    private final BiConsumer<PlayerResponse, Integer> addCallback;

    public PlayerAdapter(Context context,
                         Consumer<PlayerResponse> clickCallback,
                         BiConsumer<PlayerResponse, Integer> addCallback) {
        super(context);
        this.clickCallback = clickCallback;
        this.addCallback = addCallback;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.player_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PlayerResponse data, int position) {
        ViewHolder holder = (ViewHolder) defaultHolder;
        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);
        holder.tvName.setText(data.getName());
        holder.tvClub.setText(data.getRealClub().getName());
        holder.tvTransferValue.setText(holder.itemView.getContext().getString(R.string.money_prefix, data.getTransferValueDisplay()));
        holder.tvPoints.setText(holder.itemView.getContext().getString(R.string.point_last_round, data.getPointLastRound()));

        AppUtilities.displayPlayerPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        AppUtilities.displayPlayerPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());

        holder.ivAdd.setVisibility(visibleAddButton);
        if (visibleAddButton == View.VISIBLE) {
            boolean checked = data.getSelected();
            holder.ivAdd.setImageResource(checked ? R.drawable.ic_tick : R.drawable.ic_add_white_small);
            holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);

            mDisposable.add(RxView.clicks(holder.ivAdd).subscribe(o -> {
                Optional.from(addCallback).doIfPresent(d -> {
                    if (!data.getSelected()) {
                        d.accept(data, defaultHolder.getAdapterPosition());
                    }
                });
            }));
        }

        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o ->
                Optional.from(clickCallback).doIfPresent(d ->
                        d.accept(data))));
    }

    public void setVisibleAddButton(int visibleAddButton) {
        this.visibleAddButton = visibleAddButton;
    }

    public int findPlayerById(int playerId) {
        List<PlayerResponse> players = getDataSet();
        for (PlayerResponse player : players) {
            if (player != null && player.getId().equals(playerId)) {
                return players.indexOf(player);
            }
        }
        return -1;
    }

    static class ViewHolder extends DefaultHolder {
        @BindView(R.id.ivAvatar)
        ImageView ivAvatar;
        @BindView(R.id.tvPositionPrimary)
        ExtTextView tvPositionPrimary;
        @BindView(R.id.tvPositionSecond)
        ExtTextView tvPositionSecond;
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvClub)
        ExtTextView tvClub;
        @BindView(R.id.tvPoints)
        ExtTextView tvPoints;
        @BindView(R.id.tvTransferValue)
        ExtTextView tvTransferValue;
        @BindView(R.id.ivAdd)
        ImageView ivAdd;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
