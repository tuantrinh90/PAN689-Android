package com.bon.customview.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.bon.customview.listview.listener.ExtClickListener;
import com.bon.customview.listview.listener.ExtLoadMoreListener;
import com.bon.customview.listview.listener.ExtLongClickListener;
import com.bon.customview.listview.listener.ExtRefreshListener;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.library.R;
import com.bon.util.StringUtils;

import java.util.List;

import java8.util.function.BiConsumer;
import java8.util.function.Consumer;

/**
 * Created by Dang on 5/27/2016.
 */
@SuppressWarnings("ALL")
public class ExtPagingListView<T> extends FrameLayout {
    public static int NUMBER_PER_PAGE = 20;

    // view
    SwipeRefreshLayout rfLayout;
    ListView lvData;
    View vLoading;
    ExtTextView tvMessage;

    // status
    boolean loading = false;
    boolean hasMore = true;

    // adapter
    ExtBaseAdapter<T, ? extends ExtPagingListView.ExtViewHolder> extBaseAdapter;

    // listener
    ExtClickListener<T> onExtClickListener = null;
    ExtLongClickListener<T> onExtLongClickListener = null;
    ExtLoadMoreListener onExtLoadMoreListener = null;
    ExtRefreshListener onExtRefreshListener = null;

    /**
     * @param context
     */
    public ExtPagingListView(Context context) {
        super(context);
        initView(context, null);
    }

    /**
     * @param context
     * @param attrs
     */
    public ExtPagingListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public ExtPagingListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs);
    }

    /**
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        try {
            View view = inflate(context, R.layout.paging_listview, this);

            // refresh layout
            rfLayout = view.findViewById(R.id.rf_layout);
            rfLayout.setOnRefreshListener(() -> {
                rfLayout.setRefreshing(false);
                Optional.from(onExtRefreshListener).doIfPresent(r -> r.onRefresh());
            });

            // listview
            lvData = view.findViewById(R.id.lv_data);
            lvData.setFooterDividersEnabled(false);
            lvData.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);

            //data not found
            tvMessage = view.findViewById(R.id.tv_message);

            //attribute config
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ExtPagingListView, 0, 0);
            try {
                // swipe refresh enable
                boolean swipeRefreshEnable = typedArray.getBoolean(R.styleable.ExtPagingListView_swipeRefreshEnable, true);
                rfLayout.setEnabled(swipeRefreshEnable);

                // swipe refresh indicator color
                rfLayout.setColorSchemeColors(typedArray.getColor(R.styleable.ExtPagingListView_swipeRefreshIndicatorColor,
                        context.getResources().getColor(android.R.color.black)));

                // scroll bar visibility
                lvData.setVerticalScrollBarEnabled(typedArray.getBoolean(R.styleable.ExtPagingListView_scrollbarVisible, false));

                // divider color
                lvData.setDivider(new ColorDrawable(typedArray.getColor(R.styleable.ExtPagingListView_dividerColor, 0)));

                // divider height
                lvData.setDividerHeight(typedArray.getDimensionPixelSize(R.styleable.ExtPagingListView_dividerHeight, 0));

                // message no data
                String message = typedArray.getString(R.styleable.ExtPagingListView_noDataMessage);
                StringUtils.isNotEmpty(message, s -> tvMessage.setText(s));
            } finally {
                typedArray.recycle();
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param context
     * @param adapter
     */
    public ExtPagingListView init(Context context, ExtBaseAdapter<T, ? extends ExtPagingListView.ExtViewHolder> adapter) {
        return init(context, adapter, null);
    }

    /**
     * @param context
     * @param adapter
     * @param loadingView
     */
    @SuppressLint("InflateParams")
    public ExtPagingListView init(Context context, ExtBaseAdapter<T, ? extends ExtPagingListView.ExtViewHolder> adapter, final View loadingView) {
        try {
            extBaseAdapter = adapter;
            lvData.setAdapter(extBaseAdapter);

            // loading view
            if (loadingView != null) {
                vLoading = loadingView;
            } else {
                vLoading = LayoutInflater.from(context).inflate(R.layout.paging_item_loading, null);
            }

            // scroll listview
            lvData.setOnScrollListener(new AbsListView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {
                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                    try {
                        if (!hasMore || onExtLoadMoreListener == null) return;
                        int lastVisibleItem = visibleItemCount + firstVisibleItem;
                        if (lastVisibleItem >= totalItemCount && !loading) {
                            onExtLoadMoreListener.onLoadMore();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

            // click
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Optional.from(onExtClickListener).doIfPresent(c -> c.onClick(position, adapter == null ? null : adapter.getItem(position)));
                }
            });

            // long click
            lvData.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    Optional.from(onExtLongClickListener).doIfPresent(c -> c.onLongClick(position, adapter == null ? null : adapter.getItem(position)));
                    return false;
                }
            });

            displayMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * notification data
     *
     * @param items
     * @param <T>
     */
    public ExtPagingListView notifyDataSetChanged(List<T> items) {
        try {
            Optional.from(extBaseAdapter).doIfPresent(a -> a.notifyDataSetChanged(items));
            displayMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * display message no data
     */
    public ExtPagingListView displayMessage() {
        try {
            tvMessage.setVisibility(extBaseAdapter == null || extBaseAdapter.getCount() <= 0 ? VISIBLE : GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * get base adapter
     *
     * @return
     */
    public ExtBaseAdapter getExtBaseAdapter() {
        if (extBaseAdapter == null)
            throw new NullPointerException("ExtBaseAdapter can not null!!!");
        return extBaseAdapter;
    }

    /**
     * set adapter
     *
     * @param adapter
     * @param <AD>
     * @return
     */
    public <AD extends ExtBaseAdapter<T, ? extends ExtPagingListView.ExtViewHolder>> ExtPagingListView setExtBaseAdapter(AD adapter) {
        try {
            extBaseAdapter = adapter;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * set selected items
     *
     * @param position
     * @return
     */
    public ExtPagingListView setSelection(int position) {
        try {
            lvData.setSelection(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * @param newItem
     */
    public ExtPagingListView addNewItem(T newItem) {
        try {
            extBaseAdapter.addNewItem(newItem);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * @param newItems
     */
    public ExtPagingListView addNewItems(List<T> newItems) {
        try {
            if (newItems == null || newItems.size() < NUMBER_PER_PAGE)
                hasMore = false;
            extBaseAdapter.addNewItems(newItems);
            displayMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * remove item
     *
     * @param item
     */
    public ExtPagingListView removeItem(T item) {
        try {
            extBaseAdapter.removeItem(item);
            displayMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * remove items
     *
     * @param items
     */
    public ExtPagingListView removeItems(List<T> items) {
        try {
            extBaseAdapter.removeItems(items);
            displayMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * clear data
     */
    public ExtPagingListView clearItems() {
        try {
            hasMore = true;
            extBaseAdapter.clearList();
            displayMessage();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * start loading
     */
    public ExtPagingListView startLoading() {
        startLoading(false);
        return this;
    }

    /**
     * start loading
     */
    public ExtPagingListView startLoading(boolean isShowLoading) {
        try {
            loading = true;
            // add loading view to footer
            if (isShowLoading) lvData.addFooterView(vLoading);
            tvMessage.setVisibility(GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * stop loading
     */
    public ExtPagingListView stopLoading() {
        stopLoading(false);
        return this;
    }

    /**
     * stop loading
     */
    public ExtPagingListView stopLoading(boolean isShowLoading) {
        try {
            loading = false;
            // remove loading view
            if (isShowLoading) lvData.removeFooterView(vLoading);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * set has more
     *
     * @param hasMore
     */
    public ExtPagingListView hasMore(boolean hasMore) {
        this.hasMore = hasMore;
        return this;
    }

    /**
     * is has more items
     *
     * @return
     */
    public boolean isHasMore() {
        return hasMore;
    }

    /**
     * set refresh listview
     *
     * @param isEnabled
     * @return
     */
    public ExtPagingListView setEnabledSwipeRefreshing(boolean isEnabled) {
        try {
            rfLayout.setEnabled(isEnabled);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * scroll to position
     *
     * @param position
     * @return
     */
    public ExtPagingListView scrollToPosition(int position) {
        try {
            lvData.smoothScrollToPosition(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * set text message
     *
     * @param message
     * @return
     */
    public ExtPagingListView setMessage(String message) {
        try {
            tvMessage.setText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public ExtPagingListView setOnExtClickListener(ExtClickListener<T> onExtClickListener) {
        this.onExtClickListener = onExtClickListener;
        return this;
    }

    public ExtPagingListView setOnExtLongClickListener(ExtLongClickListener<T> onExtLongClickListener) {
        this.onExtLongClickListener = onExtLongClickListener;
        return this;
    }

    public ExtPagingListView setOnExtLoadMoreListener(ExtLoadMoreListener onExtLoadMoreListener) {
        this.onExtLoadMoreListener = onExtLoadMoreListener;
        return this;
    }

    public ExtPagingListView setOnExtRefreshListener(ExtRefreshListener onExtRefreshListener) {
        this.onExtRefreshListener = onExtRefreshListener;
        return this;
    }

    /**
     * get list view
     *
     * @return
     */
    public ListView getListView() {
        return lvData;
    }

    /**
     * view holder
     */
    public static class ExtViewHolder {
        public final View itemView;

        public ExtViewHolder(View itemView) {
            this.itemView = itemView;
        }
    }
}