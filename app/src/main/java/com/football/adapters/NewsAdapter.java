package com.football.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;
import com.football.models.News;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {
    Context context;
    List<News> news;
    Consumer<News> newsConsumer;

    public NewsAdapter(Context context, List<News> news, Consumer<News> newsConsumer) {
        this.context = context;
        this.news = news;
        this.newsConsumer = newsConsumer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;

        News item = news.get(position);
        holder.tvDay.setText(item.getDay());
        holder.tvMonth.setText(item.getMonth());
        holder.tvContent.setText(item.getContent());
        holder.itemView.setOnClickListener(v -> Optional.from(newsConsumer).doIfPresent(c -> c.accept(item)));
    }

    @Override
    public int getItemCount() {
        return news == null ? 0 : news.size();
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
