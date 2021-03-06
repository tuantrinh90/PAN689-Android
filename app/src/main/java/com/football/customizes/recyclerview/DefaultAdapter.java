package com.football.customizes.recyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.fantasy.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by toannx
 */

public abstract class DefaultAdapter<T> extends RecyclerView.Adapter<DefaultHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_LOADING = 1;

    protected final Context mContext;
    private final LayoutInflater mInflater;

    private List<T> mDataSet;
    private int loadingId;

    public DefaultAdapter(Context context) {
        this(context, new ArrayList<>());
    }

    public DefaultAdapter(Context context, List<T> dataSet) {
        this.mContext = context;
        mInflater = LayoutInflater.from(context);
        this.mDataSet = dataSet;
    }

    protected abstract int getLayoutId(int viewType);

    protected abstract DefaultHolder onCreateHolder(View v, int viewType);

    protected abstract void onBindViewHolder(@NonNull DefaultHolder defaultHolder, T data, int position);

    @Override
    public int getItemViewType(int position) {
        return getItem(position) == null ? TYPE_LOADING : TYPE_ITEM;
    }

    @NonNull
    @Override
    public DefaultHolder onCreateViewHolder(@NonNull ViewGroup parent, final int viewType) {
        if (viewType == TYPE_ITEM) {
            View view = mInflater.inflate(getLayoutId(viewType), parent, false);
            return onCreateHolder(view, viewType);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(loadingId == 0 ? R.layout.paging_item_loading : loadingId, parent, false);
            return new LoadingHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull DefaultHolder holder, int position) {
        if (getItemViewType(position) == TYPE_ITEM && getItem(position) != null) {
            onBindViewHolder(holder, getItem(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mDataSet == null ? 0 : mDataSet.size();
    }

    public void setLoadingLayout(int loadingId) {
        this.loadingId = loadingId;
    }

    public List<T> getDataSet() {
        return mDataSet;
    }

    public void setDataSet(List<T> dataSet) {
        mDataSet.clear();
        mDataSet.addAll(dataSet);
        notifyDataSetChanged();
    }

    public T getItem(int position) {
        return mDataSet == null ? null : mDataSet.get(position);
    }

    public void addItem(T item) {
        mDataSet.add(item);
        notifyItemInserted(getItemCount());
    }

    public void addItem(int index, T item) {
        mDataSet.add(index, item);
        notifyItemInserted(index);
    }

    public void addItems(List<T> items) {
        int count = getItemCount();
        mDataSet.addAll(items);
        notifyItemRangeInserted(count, items.size());
    }

    public void update(int index, T item) {
        mDataSet.set(index, item);
        notifyItemChanged(index);
    }

    public void removeItem(T item) {
        int indexOfItem = mDataSet.indexOf(item);
        removeItem(indexOfItem);
    }

    public void removeItem(int indexOfItem) {
        if (indexOfItem != -1) {
            this.mDataSet.remove(indexOfItem);
            notifyItemRemoved(indexOfItem);
        }
    }

    public void clear() {
        this.mDataSet.clear();
        notifyDataSetChanged();
    }

    public void addLoading() {
        if (!mDataSet.contains(null)) {
            addItem(null);
        }
    }

    public void removeLoading() {
        removeItem(null);
    }

    static class LoadingHolder extends DefaultHolder {

        public LoadingHolder(View itemView) {
            super(itemView);
        }

    }
}

