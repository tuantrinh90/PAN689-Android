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
import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class PlayerAdapter extends ExtBaseAdapter<PlayerResponse, PlayerAdapter.ViewHolder> {

    private CompositeDisposable mDisposable = new CompositeDisposable();
    private Consumer<PlayerResponse> responseConsumer;
    private Consumer<PlayerResponse> addConsumer;

    private Set<Integer> checkedList = new HashSet<>();

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
        setPosition(holder.tvPositionPrimary, data.getMainPosition(), data.getMainPositionText());
        setPosition(holder.tvPositionSecond, data.getMinorPosition(), data.getMinorPositionText());
        boolean checked = data.getSelected();
        holder.ivAdd.setImageResource(checked ? R.drawable.ic_tick : R.drawable.ic_add_white_small);
        holder.ivAdd.setBackgroundResource(checked ? R.drawable.bg_circle_white_border : R.drawable.bg_circle_yellow);

        mDisposable.add(RxView.clicks(holder.ivAdd).subscribe(o -> {
            Optional.from(addConsumer).doIfPresent(d -> {
                if (!isChecked(data.getId())) {
                    checkedList.add(data.getId());
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

    private boolean isChecked(int playerId) {
        return checkedList.contains(playerId);
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
