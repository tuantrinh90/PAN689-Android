package com.football.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.football.customizes.carousels.Carousel;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarouselDotAdapter extends RecyclerView.Adapter<CarouselDotAdapter.ViewHolder> {
    final Context context;
    List<Carousel> items;
    int colorActive;
    int colorNormal;

    public CarouselDotAdapter(Context context, List<Carousel> items,
                              int colorActive, int colorNormal) {
        this.context = context;
        this.items = items;
        this.colorActive = colorActive;
        this.colorNormal = colorNormal;
    }

    public void notifyDataSetChanged(List<Carousel> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    public List<Carousel> getItems() {
        return items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.carousel_dot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;
        Carousel carousel = items.get(position);
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_carousel_dot);
        drawable.setColorFilter(ContextCompat.getColor(context, carousel.isActive() ? colorActive : colorNormal), PorterDuff.Mode.SRC_ATOP);
        holder.llDot.setBackground(drawable);
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.llDot)
        LinearLayout llDot;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
