package com.football.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.carousels.Carousel;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class CarouselAdapter extends RecyclerView.Adapter<CarouselAdapter.ViewHolder> {
    final Context context;
    List<Carousel> items;
    int colorActive;
    int colorNormal;
    Consumer<Integer> positionConsumer;

    public CarouselAdapter(Context context, List<Carousel> items,
                           int colorActive, int colorNormal, Consumer<Integer> positionConsumer) {
        this.context = context;
        this.items = items;
        this.colorActive = colorActive;
        this.colorNormal = colorNormal;
        this.positionConsumer = positionConsumer;
    }

    public void notifyDataSetChanged(List<Carousel> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public List<Carousel> getItems() {
        return items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.carousel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;
        Carousel carousel = items.get(position);
        holder.tvContent.setText(carousel.getContent());
        holder.tvContent.setTextColor(ContextCompat.getColor(context, carousel.isActive() ? colorActive : colorNormal));
        holder.itemView.setOnClickListener(v -> Optional.from(positionConsumer).doIfPresent(consumer -> consumer.accept(position)));
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tvContent)
        ExtTextView tvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
