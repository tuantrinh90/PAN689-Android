package com.football.adapters;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
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

public class PlayerPoolItemAdapter extends ExtBaseAdapter<PlayerResponse, PlayerPoolItemAdapter.ViewHolder> {

    private final Consumer<PlayerResponse> clickConsumer;
    private CompositeDisposable mDisposable;

    /**
     * @param ctx
     * @param its
     */
    public PlayerPoolItemAdapter(Context ctx, List<PlayerResponse> its, Consumer<PlayerResponse> clickConsumer) {
        super(ctx, its);
        this.clickConsumer = clickConsumer;

        mDisposable = new CompositeDisposable();
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
        holder.tvPoint.setText(String.valueOf(data.getPointLastRound()));
        AppUtilities.displayPlayerPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        AppUtilities.displayPlayerPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());
        boolean checked = data.getSelected();
        holder.ivAdd.setImageResource(checked ? R.drawable.ic_tick : R.drawable.ic_add_white_small);
        holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);

        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o ->
                Optional.from(clickConsumer).doIfPresent(d ->
                        d.accept(data))));
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
