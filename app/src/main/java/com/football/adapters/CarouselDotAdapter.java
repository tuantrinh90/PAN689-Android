package com.football.adapters;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.customizes.carousels.Carousel;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CarouselDotAdapter extends BaseRecyclerViewAdapter<Carousel, CarouselDotAdapter.ViewHolder> {
    int colorActive;
    int colorNormal;

    public CarouselDotAdapter(Context context, List<Carousel> items,
                              int colorActive, int colorNormal) {
        super(context, items);
        this.colorActive = colorActive;
        this.colorNormal = colorNormal;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.carousel_dot, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carousel carousel = getItem(position);
        if (carousel == null) return;

        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.bg_carousel_dot);
        drawable.setColorFilter(ContextCompat.getColor(context, carousel.isActive() ? colorActive : colorNormal), PorterDuff.Mode.SRC_ATOP);
        holder.llDot.setBackground(drawable);
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
