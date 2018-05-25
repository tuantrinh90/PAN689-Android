package com.football.common.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import java.util.List;

public abstract class BaseRecyclerViewAdapter<T, V extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<V> {
    protected Context context;
    protected LayoutInflater layoutInflater;
    protected List<T> items;

    public BaseRecyclerViewAdapter(Context context, List<T> its) {
        this.context = context;
        this.items = its;
        this.layoutInflater = LayoutInflater.from(context);
    }

    /**
     * notify data set
     *
     * @param its
     */
    public void notifyDataSetChanged(List<T> its) {
        items = its;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items == null ? 0 : items.size();
    }

    /**
     * @param position
     * @return
     */
    protected T getItem(int position) {
        if (getItemCount() <= 0) return null;
        return items.get(position);
    }

    /**
     * get items
     *
     * @return
     */
    public List<T> getItems() {
        return items;
    }
}
