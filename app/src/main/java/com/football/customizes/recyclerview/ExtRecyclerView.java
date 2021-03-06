package com.football.customizes.recyclerview;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bon.customview.listview.listener.ExtLoadMoreListener;
import com.bon.customview.listview.listener.ExtRefreshListener;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.fantasy.R;

import java.util.List;

public class ExtRecyclerView<T> extends FrameLayout {

    private static final String TAG = "ExtRecyclerView";

    // view
    private SwipeRefreshLayout rfLayout;
    private RecyclerView recyclerView;
    private ExtTextView tvMessage;

    private Context context;
    private DefaultAdapter<T> mAdapter;
    private EndlessScrollListener endlessScrollListener;

    @LayoutRes
    private int loadingLayout;
    private String message;

    // listener
    private ExtLoadMoreListener onExtLoadMoreListener = null;
    private ExtRefreshListener onExtRefreshListener = null;
    private RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false);
    private int perPage = 20;
    private boolean hasFixedSize;

    public ExtRecyclerView(@NonNull Context context) {
        this(context, null, 0);
    }

    public ExtRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ExtRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        try {
            View view = inflate(context, R.layout.widget_ext_recyclerview, this);

            // refresh layout
            rfLayout = view.findViewById(R.id.rf_layout);
            rfLayout.setOnRefreshListener(() -> {
                if (endlessScrollListener != null) {
                    endlessScrollListener.resetState();
                }
                tvMessage.setVisibility(GONE);
                Optional.from(onExtRefreshListener).doIfPresent(ExtRefreshListener::onRefresh);
            });

            // recyclerView
            recyclerView = view.findViewById(R.id.recyclerview);

            // setAnimation
            LayoutAnimationController animation = AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);
            recyclerView.setLayoutAnimation(animation);

            //data not found
            tvMessage = view.findViewById(R.id.tv_message);
            tvMessage.setVisibility(GONE);

            //attribute config
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtRecyclerView, 0, 0);
            try {
                // swipe refresh enable
                boolean swipeRefreshEnable = typedArray.getBoolean(R.styleable.ExtRecyclerView_swipeRefreshEnable, true);
                rfLayout.setEnabled(swipeRefreshEnable);

                // swipe refresh indicator color
                rfLayout.setColorSchemeColors(typedArray.getColor(R.styleable.ExtRecyclerView_swipeRefreshIndicatorColor,
                        context.getResources().getColor(android.R.color.black)));

                // scroll bar visibility
                recyclerView.setVerticalScrollBarEnabled(typedArray.getBoolean(R.styleable.ExtRecyclerView_scrollbarVisible, false));

                Divider divider = new Divider();
                // divider color
                ColorDrawable colorDrawable = new ColorDrawable(typedArray.getColor(R.styleable.ExtRecyclerView_dividerColor, 0));
                divider.setDivider(colorDrawable);

                // divider height
                int dividerHeight = typedArray.getDimensionPixelSize(R.styleable.ExtRecyclerView_dividerHeight, 0);
                divider.setHeight(dividerHeight);

                recyclerView.addItemDecoration(divider);

                // message no data
                this.message = typedArray.getString(R.styleable.ExtRecyclerView_noDataMessage);
                if (TextUtils.isEmpty(message)) {
                    message = context.getString(R.string.no_data);
                }
            } finally {
                typedArray.recycle();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public ExtRecyclerView adapter(DefaultAdapter<T> adapter) {
        this.mAdapter = adapter;
        return this;
    }

    public ExtRecyclerView perPage(int perPage) {
        this.perPage = perPage;
        return this;
    }

    public ExtRecyclerView hasFixedSize(boolean hasFixedSize) {
        this.hasFixedSize = hasFixedSize;
        return this;
    }

    public ExtRecyclerView layoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        return this;
    }

    public ExtRecyclerView loadMoreListener(ExtLoadMoreListener onExtLoadMoreListener) {
        this.onExtLoadMoreListener = onExtLoadMoreListener;
        return this;
    }

    public ExtRecyclerView refreshListener(ExtRefreshListener onExtRefreshListener) {
        this.onExtRefreshListener = onExtRefreshListener;
        return this;
    }

    public ExtRecyclerView loadingLayout(@LayoutRes int loadingLayout) {
        this.loadingLayout = loadingLayout;
        return this;
    }

    public ExtRecyclerView noDataMessage(String message) {
        this.message = message;
        return this;
    }

    public void build() {
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(mAdapter);
        mAdapter.setLoadingLayout(loadingLayout);
        if (onExtRefreshListener == null) {
            rfLayout.setEnabled(false);
        }

        endlessScrollListener = new EndlessScrollListener(recyclerView.getLayoutManager()) {
            @Override
            public void onLoadMore(int page) {
                if (onExtLoadMoreListener != null) {
                    onExtLoadMoreListener.onLoadMore();
                }
            }
        };
        recyclerView.addOnScrollListener(endlessScrollListener);
        recyclerView.setHasFixedSize(hasFixedSize);
    }

    public void displayMessage() {
        try {
            tvMessage.setVisibility(mAdapter == null || mAdapter.getItemCount() <= 0 ? VISIBLE : GONE);
            tvMessage.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeLoading() {
        mAdapter.removeLoading();
    }

    public void clear() {
        mAdapter.clear();
    }

    public void startLoading() {
        rfLayout.setRefreshing(true);
        tvMessage.setVisibility(GONE);
    }

    public void stopLoading() {
        rfLayout.setRefreshing(false);
    }

    public void setDataSet(List<T> dataSet) {
        mAdapter.setDataSet(dataSet);
    }

    public T getItem(int position) {
        return mAdapter.getItem(position);
    }

    public void addItem(T item) {
        mAdapter.addItem(item);
    }

    public void addItem(int index, T item) {
        mAdapter.addItem(index, item);
    }

    public void addItems(@NonNull List<T> items) {
        stopLoading();
        if (onExtLoadMoreListener != null) {
            removeLoading();

            // add loading
            if (items.size() == perPage || items.size() != 0) {
                items.add(null);
            }
        }
        boolean animation = mAdapter.getItemCount() == 0;
        mAdapter.addItems(items);
        if (animation) {
            recyclerView.scheduleLayoutAnimation();
        }
        displayMessage();
    }

    public void addItemsWithLoading(List<T> items) {
        stopLoading();
        if (onExtLoadMoreListener != null) {
            removeLoading();

            // add loading
            if (items.size() == perPage || items.size() != 0) {
                items.add(null);
            } else {
                items.add(null);
                endlessScrollListener.increaseCurrentPage();
                endlessScrollListener.onLoadMore(endlessScrollListener.getCurrentPage());
            }
        }
        mAdapter.addItems(items);
        displayMessage();
    }

    public void update(int index, T item) {
        mAdapter.update(index, item);
    }

    public void removeItem(T item) {
        mAdapter.removeItem(item);
    }

    public void removeItem(int indexOfItem) {
        mAdapter.removeItem(indexOfItem);
    }

    public DefaultAdapter<T> getAdapter() {
        return mAdapter;
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
        displayMessage();
    }
}
