package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
        if (position == 0 || !getItem(position - 1).getNotificationDate().equals(data.getNotificationDate())) {
            holder.header.setVisibility(View.VISIBLE);
            holder.textDate.setText(AppUtilities.getDate(data.getNotificationTime()));
        } else {
            holder.header.setVisibility(View.GONE);
        }

        holder.textContent.setText(Html.fromHtml(data.getTitle()));
        holder.textTime.setText(AppUtilities.getRelativeTimeSpanString(data.getNotificationTime()));
        displayType(holder, data.getType());
    }

    private void displayType(NotificationHolder holder, String type) {
        // display background & icon
        switch (type) {
            case NotificationResponse.TYPE_INFO:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_green_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_info);
                break;
            case NotificationResponse.TYPE_ACCEPT:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_blue_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_check);
                break;
            case NotificationResponse.TYPE_REJECT:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_red_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_close);
                break;
            case NotificationResponse.TYPE_EMAIL:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_green_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_email);
                break;
            case NotificationResponse.TYPE_LEAGUE:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_green_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_league_start);
                break;
            case NotificationResponse.TYPE_LEAGUE_START:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_green_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_league_start);
                break;
            case NotificationResponse.TYPE_LEAGUE_FINISH:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_green_button_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_league_finish);
                break;
            case NotificationResponse.TYPE_WARNING:
                holder.iconBackground.setBackgroundResource(R.drawable.bg_type_warning_radius);
                holder.icon.setImageResource(R.drawable.ic_noti_warning);
                break;
        }
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
        @BindView(R.id.image_icon)
        ImageView icon;
        @BindView(R.id.icon_background)
        FrameLayout iconBackground;

        public NotificationHolder(View itemView) {
            super(itemView);
        }
    }
}
