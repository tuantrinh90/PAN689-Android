package com.football.customizes.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by toannx
 */

public abstract class DefaultHolder<T> extends RecyclerView.ViewHolder {
    protected final String TAG = this.getClass().getSimpleName();

    private OnHolderClickListener mOnHolderClickListener;

    public DefaultHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public abstract void bind(T data, int position);

    /**
     * từ DefHolderImpl bắn sự kiện click về cho DefHolderImpl
     * @param view: muốn bắn thằng nào thì gửi vào đây
     */
    protected void dispatchClickEvent(View view) {
        if (mOnHolderClickListener != null) {
            mOnHolderClickListener.onHolderClick(view, getAdapterPosition());
        }
    }

    void setOnHolderClickListener(OnHolderClickListener listener) {
        this.mOnHolderClickListener = listener;
    }

    interface OnHolderClickListener {
        void onHolderClick(View view, int position);
    }
}

