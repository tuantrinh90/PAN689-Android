package com.football.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.FriendResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class InviteFriendAdapter extends ExtBaseAdapter<FriendResponse, InviteFriendAdapter.ViewHolder> {
    Consumer<FriendResponse> detailConsumer;
    Consumer<FriendResponse> inviteConsumer;

    public InviteFriendAdapter(Context context, List<FriendResponse> friendResponses,
                               Consumer<FriendResponse> detailConsumer, Consumer<FriendResponse> inviteConsumer) {
        super(context, friendResponses);
        this.detailConsumer = detailConsumer;
        this.inviteConsumer = inviteConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.invite_friend_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, FriendResponse friendResponse) {
        holder.ivAvatar.setImageUri(friendResponse.getPhoto());
        holder.tvName.setText(friendResponse.getName());
        holder.tvStatus.setEnabled(!friendResponse.getInvited());

        if (friendResponse.getInvited()) {
            holder.tvStatus.setBackground(null);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_content));
            holder.tvStatus.setEnabled(false);
            holder.tvStatus.setText(R.string.invited);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_blue_radius_selector);
            holder.tvStatus.setEnabled(true);
            holder.tvStatus.setText(R.string.invite);
        }

        // click
        RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailConsumer).doIfPresent(d -> d.accept(friendResponse)));
        RxView.clicks(holder.tvStatus).subscribe(o -> Optional.from(inviteConsumer).doIfPresent(d -> d.accept(friendResponse)));
    }

    static class ViewHolder extends ExtPagingListView.ExtViewHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvStatus)
        ExtTextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
