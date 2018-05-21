package com.football.customizes.carousels;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.SnapHelper;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.recycleview.centering_recyclerview.CenteringRecyclerView;
import com.football.adapters.CarouselAdapter;
import com.football.adapters.CarouselDotAdapter;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class CarouselView extends LinearLayout {
    private static final String TAG = CarouselView.class.getSimpleName();

    @BindView(R.id.rvRecyclerView)
    CenteringRecyclerView rvRecyclerView;
    @BindView(R.id.rvRecyclerViewDot)
    CenteringRecyclerView rvRecyclerViewDot;

    CarouselAdapter carouselAdapter;
    CarouselDotAdapter carouselDotAdapter;
    boolean isTextAllCaps = true;
    String fontPath = "";

    public CarouselView(Context context) {
        super(context);
        initView(context, null);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public CarouselView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CarouselView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    void initView(Context context, AttributeSet attrs) {
        View view = LayoutInflater.from(context).inflate(R.layout.carousel_view, this);
        ButterKnife.bind(this, view);
    }

    /**
     * @param context
     * @param items
     * @param positionConsumer
     */
    public void setAdapter(Context context, List<Carousel> items, int colorActive, int colorNormal, Consumer<Integer> positionConsumer) {
        carouselAdapter = new CarouselAdapter(context, items, colorActive, colorNormal, positionConsumer, isTextAllCaps, fontPath);
        rvRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvRecyclerView.setAdapter(carouselAdapter);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvRecyclerView);

        carouselDotAdapter = new CarouselDotAdapter(context, items, colorActive, colorNormal);
        rvRecyclerViewDot.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        rvRecyclerViewDot.setAdapter(carouselDotAdapter);
    }

    /**
     * @param position
     */
    public void setActivePosition(int position) {
        if (carouselAdapter == null || carouselAdapter.getItems() == null)
            throw new NullPointerException();

        // update selected item
        List<Carousel> items = carouselAdapter.getItems();
        for (int i = 0; i < items.size(); i++) {
            items.get(i).setActive(i == position);
        }

        carouselAdapter.notifyDataSetChanged(items);
        carouselDotAdapter.notifyDataSetChanged(items);

        // move recycle to item selected
        rvRecyclerView.center(position);
    }

    /**
     * @param isTextAllCaps
     * @return
     */
    public CarouselView setTextAllCaps(boolean isTextAllCaps) {
        this.isTextAllCaps = isTextAllCaps;
        return this;
    }

    /**
     * @param fontPath
     * @return
     */
    public CarouselView setFontPath(String fontPath) {
        this.fontPath = fontPath;
        return this;
    }
}
