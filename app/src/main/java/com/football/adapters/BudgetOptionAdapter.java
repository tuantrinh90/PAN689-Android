package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.fantasy.R;
import com.football.models.responses.BudgetResponse;
import com.football.utilities.AppUtilities;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class BudgetOptionAdapter extends BaseRecyclerViewAdapter<BudgetResponse, BudgetOptionAdapter.ViewHolder> {

    Consumer<BudgetResponse> consumer;

    private boolean enable = true;

    public BudgetOptionAdapter(Context context, List<BudgetResponse> its, Consumer<BudgetResponse> consumer) {
        super(context, its);
        this.consumer = consumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.budget_option_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BudgetResponse data = getItem(position);
        if (data == null) return;

        holder.tvBudgetOption.setText(data.getName());
        holder.tvBudgetOptionValue.setText(String.format(context.getString(R.string.money_prefix), AppUtilities.getMoney(data.getValue())));
        holder.itemView.setActivated(data.getIsActivated());
        holder.tvBudgetOption.setActivated(data.getIsActivated());
        holder.tvBudgetOptionValue.setActivated(data.getIsActivated());
        holder.itemView.setOnClickListener(v -> {
            if (enable) {
                Optional.from(consumer).doIfPresent(c -> c.accept(data));
            }
        });
    }

    public void clickEnable(boolean enable) {
        this.enable = enable;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvBudgetOption)
        ExtTextView tvBudgetOption;
        @BindView(R.id.tvBudgetOptionValue)
        ExtTextView tvBudgetOptionValue;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
