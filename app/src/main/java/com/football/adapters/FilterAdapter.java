package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class FilterAdapter extends BaseRecyclerViewAdapter<ExtKeyValuePair, FilterAdapter.ViewHolder> {
    private Consumer<ExtKeyValuePair> valuePairConsumer;
    private int checkCount = 0;

    public FilterAdapter(Context context, List<ExtKeyValuePair> its, Consumer<ExtKeyValuePair> valuePairConsumer) {
        super(context, its);
        this.valuePairConsumer = valuePairConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.filter_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ExtKeyValuePair keyValuePair = getItem(position);
        if (keyValuePair == null) return;

        holder.ivContent.setActivated(keyValuePair.isSelected());
        holder.tvContent.setText(keyValuePair.getValue());
        holder.itemView.setOnClickListener(v -> Optional.from(valuePairConsumer).doIfPresent(c -> c.accept(keyValuePair)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivContent)
        ImageView ivContent;
        @BindView(R.id.tvContent)
        ExtTextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
