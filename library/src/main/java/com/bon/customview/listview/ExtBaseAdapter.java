package com.bon.customview.listview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dang on 5/27/2016.
 */

public abstract class ExtBaseAdapter<T, VH extends ExtPagingListView.ExtViewHolder> extends BaseAdapter {
    protected LayoutInflater layoutInflater;
    protected Context context;
    protected List<T> items;

    /**
     * @param ctx
     * @param its
     */
    public ExtBaseAdapter(Context ctx, List<T> its) {
        context = ctx;
        items = its;
        layoutInflater = LayoutInflater.from(context);
    }

    protected abstract int getViewId();

    protected abstract VH onCreateViewHolder(View view);

    protected abstract void onBindViewHolder(VH viewHolder, T data);

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (getViewId() <= 0 || getCount() <= 0) return null;
        VH viewHolder;

        if (convertView == null) {
            convertView = layoutInflater.inflate(getViewId(), parent, false);
            // create view holder
            viewHolder = onCreateViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (VH) convertView.getTag();
        }

        // bind data
        onBindViewHolder(viewHolder, getItem(position));

        // view
        return convertView;
    }

    /**
     * notification data
     *
     * @param its
     */
    public void notifyDataSetChanged(List<T> its) {
        try {
            items = its;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param newItem
     */
    public void addNewItem(T newItem) {
        try {
            if (newItem == null) return;
            if (items == null) items = new ArrayList<>();
            items.add(newItem);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param newItems
     */
    public void addNewItems(List<T> newItems) {
        try {
            if (newItems == null || newItems.size() <= 0) return;
            if (items == null) items = new ArrayList<>();
            items.addAll(newItems);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * remove item
     *
     * @param item
     */
    public void removeItem(T item) {
        try {
            if (item == null || items == null || items.size() <= 0) return;
            items.remove(item);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * remove items
     *
     * @param its
     */
    public void removeItems(List<T> its) {
        try {
            if (its == null || its.size() <= 0 || items == null || items.size() <= 0) return;
            items.removeAll(its);
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * clear data
     */
    public void clearList() {
        try {
            if (items == null || items.size() <= 0) return;
            items.clear();
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return items == null ? 0 : items.size();
    }

    @Override
    public T getItem(int position) {
        try {
            return items == null ? null : items.get(position);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public List<T> getItems() {
        return items;
    }
}
