package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class PlayerAdapter extends ExtBaseAdapter<PlayerResponse, PlayerAdapter.ViewHolder> {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Consumer<PlayerResponse> responseConsumer;
    private Consumer<PlayerResponse> addConsumer;

    public PlayerAdapter(Context context, List<PlayerResponse> playerResponses,
                         Consumer<PlayerResponse> responseConsumer, Consumer<PlayerResponse> addConsumer) {
        super(context, playerResponses);
        this.responseConsumer = responseConsumer;
        this.addConsumer = addConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.player_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, PlayerResponse data) {
        ImageLoaderUtils.displayImage(data.getPhoto(), holder.ivAvatar);
        holder.tvName.setText(data.getName());
        holder.tvClub.setText(data.getRealClub().getName());
        holder.tvTransferValue.setText(holder.itemView.getContext().getString(R.string.money_prefix, data.getTransferValueDisplay()));
        holder.tvPoints.setText(holder.itemView.getContext().getString(R.string.point_last_round, data.getPointLastRound()));
        AppUtilities.displayPlayerPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        AppUtilities.displayPlayerPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());
        boolean checked = data.getSelected();
        holder.ivAdd.setImageResource(checked ? R.drawable.ic_tick : R.drawable.ic_add_white_small);
        holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);

        mDisposable.add(RxView.clicks(holder.ivAdd).subscribe(o -> {
            Optional.from(addConsumer).doIfPresent(d -> {
                if (!checked) {
                    d.accept(data);
                    holder.ivAdd.setImageResource(R.drawable.ic_tick);
                    holder.ivAdd.setBackgroundResource(R.drawable.bg_circle_white_border);
                }
            });
        }));
        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o ->
                Optional.from(responseConsumer).doIfPresent(d ->
                        d.accept(data))));
    }

    static class ViewHolder extends ExtPagingListView.ExtViewHolder {

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
            ButterKnife.bind(this, itemView);
        }
    }
}
