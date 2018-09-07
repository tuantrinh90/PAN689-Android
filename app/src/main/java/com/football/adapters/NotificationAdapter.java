package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.NotificationResponse;
import com.football.utilities.AppUtilities;

import butterknife.BindView;

public class NotificationAdapter extends DefaultAdapter<NotificationResponse> {

    public NotificationAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.notification_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new NotificationHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, NotificationResponse data, int position) {
        NotificationHolder holder = (NotificationHolder) defaultHolder;
        if (position == 0 || !getItem(position - 1).getCreatedDate().equals(data.getCreatedDate())) {
            holder.header.setVisibility(View.VISIBLE);
            holder.textDate.setText(AppUtilities.getDate(data.getCreatedAt()));
        } else {
            holder.header.setVisibility(View.GONE);
        }

        holder.textContent.setText(Html.fromHtml(data.getTitleHtml()));
    }

    static class NotificationHolder extends DefaultHolder {

        @BindView(R.id.text_date)
        ExtTextView textDate;
        @BindView(R.id.header)
        LinearLayout header;
        @BindView(R.id.text_content)
        ExtTextView textContent;
        @BindView(R.id.text_time)
        ExtTextView textTime;

        public NotificationHolder(View itemView) {
            super(itemView);
        }
    }
}
