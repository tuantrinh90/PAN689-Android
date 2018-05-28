package com.bon.customview.keyvaluepair;

import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.library.R;
import com.bon.logger.Logger;
import com.bon.util.FontUtils;
import com.bon.util.TextUtils;

import java.util.List;

/**
 * Created by Administrator on 12/01/2017.
 */

public class ExtKeyValuePairAdapter<T extends ExtKeyValuePair> extends ExtBaseAdapter<T, ExtKeyValuePairAdapter.ViewHolder<T>> {
    private static final String TAG = ExtKeyValuePairAdapter.class.getSimpleName();

    // text gravity
    private int textGravity = Gravity.CENTER;

    /**
     * @param items
     */
    public ExtKeyValuePairAdapter(Context context, List<T> items) {
        super(context, items);
    }

    /**
     * @param items
     */
    public ExtKeyValuePairAdapter(Context context, List<T> items, int textGravity) {
        super(context, items);
        this.textGravity = textGravity;
    }

    @Override
    protected int getViewId() {
        return R.layout.key_value_pair_row;
    }

    @Override
    protected ViewHolder<T> onCreateViewHolder(View view) {
        return new ViewHolder<>(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder<T> viewHolder, T data) {
        viewHolder.setData(context, data, textGravity);
    }

    static class ViewHolder<T extends ExtKeyValuePair> extends ExtPagingListView.ExtViewHolder {
        ExtTextView tvContent;

        ViewHolder(View view) {
            super(view);
            tvContent = view.findViewById(R.id.tvContent);
        }

        public void setData(Context context, T keyValuePair, int textGravity) {
            try {
                if (keyValuePair == null) return;

                // selected
                if (keyValuePair.isSelected()) {
                    TextUtils.setTextAppearance(context, tvContent, R.style.StyleContentBold);
                    FontUtils.setCustomTypeface(context, tvContent, context.getString(R.string.font_display_bold));
                } else {
                    TextUtils.setTextAppearance(context, tvContent, R.style.StyleContent);
                    FontUtils.setCustomTypeface(context, tvContent, context.getString(R.string.font_display_regular));
                }

                // content
                tvContent.setText(keyValuePair.getValue());
                tvContent.setGravity(textGravity);
                tvContent.setTextColor(keyValuePair.getTextColor());
            } catch (Exception e) {
                Logger.e(TAG, e);
            }
        }
    }
}
