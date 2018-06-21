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
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.bon.customview.listview.listener.ExtLoadMoreListener;
import com.bon.customview.listview.listener.ExtRefreshListener;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.StringUtils;
import com.football.fantasy.R;

import java.util.List;

public class ExtRecyclerView<T> extends FrameLayout {

    private static final String TAG = "ExtRecyclerView";

    // view
    SwipeRefreshLayout rfLayout;
    RecyclerView recyclerView;
    View vLoading;
    ExtTextView tvMessage;

    private Context context;
    private DefaultAdapter<T> mAdapter;
    private EndlessScrollListener endlessScrollListener;

    // listener
    ExtLoadMoreListener onExtLoadMoreListener = null;
    ExtRefreshListener onExtRefreshListener = null;


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
                endlessScrollListener.resetState();
                Optional.from(onExtRefreshListener).doIfPresent(ExtRefreshListener::onRefresh);
            });

            // recyclerView
            recyclerView = view.findViewById(R.id.recyclerview);

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
                String message = typedArray.getString(R.styleable.ExtRecyclerView_noDataMessage);
                StringUtils.isNotEmpty(message, s -> tvMessage.setText(s));
            } finally {
                typedArray.recycle();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    public ExtRecyclerView init(Context context, DefaultAdapter<T> adapter) {
        return init(context, adapter, null);
    }

    public ExtRecyclerView init(Context context, DefaultAdapter<T> adapter, final View loadingView) {
        try {
            this.mAdapter = adapter;
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayout.VERTICAL, false));
            recyclerView.setAdapter(mAdapter);

            // loading view
            if (loadingView != null) {
                vLoading = loadingView;
            } else {
                vLoading = LayoutInflater.from(context).inflate(R.layout.paging_item_loading, null);
            }

            endlessScrollListener = new EndlessScrollListener(recyclerView.getLayoutManager()) {
                @Override
                public void onLoadMore(int page) {
                    if (onExtLoadMoreListener != null) {
                        onExtLoadMoreListener.onLoadMore();
                        recyclerView.post(() -> mAdapter.addLoading());
                    }

                }
            };
            recyclerView.addOnScrollListener(endlessScrollListener);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public void displayMessage() {
        try {
            tvMessage.setVisibility(mAdapter == null || mAdapter.getItemCount() <= 0 ? VISIBLE : GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeLoading() {
        mAdapter.removeLoading();
    }

    public ExtRecyclerView setOnExtLoadMoreListener(ExtLoadMoreListener onExtLoadMoreListener) {
        this.onExtLoadMoreListener = onExtLoadMoreListener;
        return this;
    }

    public ExtRecyclerView setOnExtRefreshListener(ExtRefreshListener onExtRefreshListener) {
        this.onExtRefreshListener = onExtRefreshListener;
        return this;
    }

    public ExtRecyclerView setLoadingLayout(@LayoutRes int loadingLayout) {
        this.mAdapter.setLoadingLayout(loadingLayout);
        return this;
    }

    public void clear() {
        mAdapter.clear();
    }

    public void startLoading() {
        rfLayout.setRefreshing(true);
    }

    public void stopLoading() {
        rfLayout.setRefreshing(false);
    }

    public void addItems(List<T> items) {
        mAdapter.addItems(items);
        displayMessage();
        if (onExtLoadMoreListener != null) {
            removeLoading();
        }
    }

    public DefaultAdapter<T> getAdapter() {
        return mAdapter;
    }

    public void notifyDataSetChanged() {
        mAdapter.notifyDataSetChanged();
        displayMessage();
    }
}
