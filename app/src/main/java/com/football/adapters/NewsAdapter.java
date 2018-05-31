package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.GeneralUtils;
import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.fantasy.R;
import com.football.models.responses.NewsResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class NewsAdapter extends BaseRecyclerViewAdapter<NewsResponse, NewsAdapter.ViewHolder> {
    Consumer<NewsResponse> newsConsumer;

    public NewsAdapter(Context context, List<NewsResponse> newsResponses, Consumer<NewsResponse> newsConsumer) {
        super(context, newsResponses);
        this.newsConsumer = newsConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewsResponse item = getItem(position);
        assert item != null;

        holder.tvDay.setText(item.getDay());
        holder.tvMonth.setText(item.getMonth());
        holder.tvContent.setText(item.getTitle());
        RxView.clicks(holder.itemView).subscribe(v -> Optional.from(newsConsumer).doIfPresent(c -> c.accept(item)));

        // update layout
        int marginLayout = GeneralUtils.convertDpMeasureToPixel(context, R.dimen.padding_layout);
        RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(marginLayout, 0, marginLayout, marginLayout / 2);
        holder.itemView.setLayoutParams(layoutParams);
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvDay)
        ExtTextView tvDay;
        @BindView(R.id.tvMonth)
        ExtTextView tvMonth;
        @BindView(R.id.tvContent)
        ExtTextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
