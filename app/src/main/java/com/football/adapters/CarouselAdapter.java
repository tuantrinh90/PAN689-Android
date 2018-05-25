package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.FontUtils;
import com.bon.util.StringUtils;
import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.customizes.carousels.Carousel;
import com.football.fantasy.R;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class CarouselAdapter extends BaseRecyclerViewAdapter<Carousel, CarouselAdapter.ViewHolder> {
    int colorActive;
    int colorNormal;
    Consumer<Integer> positionConsumer;
    boolean isTextAllCaps;
    String fontPath;

    public CarouselAdapter(Context context, List<Carousel> items,
                           int colorActive, int colorNormal, Consumer<Integer> positionConsumer, boolean isTextAllCaps, String fontPath) {
        super(context, items);
        this.colorActive = colorActive;
        this.colorNormal = colorNormal;
        this.positionConsumer = positionConsumer;
        this.isTextAllCaps = isTextAllCaps;
        this.fontPath = fontPath;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.carousel_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Carousel carousel = getItem(position);
        if (carousel == null) return;

        holder.tvContent.setText(isTextAllCaps ? carousel.getContent().toUpperCase() : carousel.getContent());
        holder.tvContent.setTextColor(ContextCompat.getColor(context, carousel.isActive() ? colorActive : colorNormal));

        // update font
        if (!StringUtils.isEmpty(fontPath)) {
            FontUtils.setCustomTypeface(context, holder.tvContent, fontPath);
        }

        // click view
        RxView.clicks(holder.itemView).subscribe(o -> Optional.from(positionConsumer).doIfPresent(consumer -> consumer.accept(position)));
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
