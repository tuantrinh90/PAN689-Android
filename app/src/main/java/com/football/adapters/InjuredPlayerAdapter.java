package com.football.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.PlayerResponse;

import butterknife.BindView;
import java8.util.function.Consumer;

public class InjuredPlayerAdapter extends DefaultAdapter<PlayerResponse> {

    private Consumer<PlayerResponse> deleteConsumer;

    public InjuredPlayerAdapter(Consumer<PlayerResponse> deleteConsumer) {
        this.deleteConsumer = deleteConsumer;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_squad_trade_transferring_injured_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new InjuredHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, PlayerResponse data, int position) {
        InjuredHolder holder = (InjuredHolder) defaultHolder;
        holder.tvName.setText(data.getName());
        holder.ivDelete.setOnClickListener(v -> deleteConsumer.accept(getItem(defaultHolder.getAdapterPosition())));
    }

    static class InjuredHolder extends DefaultHolder {

        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.ivDelete)
        ImageView ivDelete;

        public InjuredHolder(View itemView) {
            super(itemView);
        }
    }
}
