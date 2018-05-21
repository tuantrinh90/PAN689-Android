package com.football.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.Friend;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class InviteFriendAdapter extends RecyclerView.Adapter<InviteFriendAdapter.ViewHolder> {
    Context context;
    List<Friend> friends;
    Consumer<Friend> detailConsumer;
    Consumer<Friend> inviteConsumer;

    public InviteFriendAdapter(Context context, List<Friend> friends,
                               Consumer<Friend> detailConsumer, Consumer<Friend> inviteConsumer) {
        this.context = context;
        this.friends = friends;
        this.detailConsumer = detailConsumer;
        this.inviteConsumer = inviteConsumer;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.invite_friend_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;

        Friend friend = friends.get(position);
        holder.ivAvatar.setImageUri(friend.getAvatar());
        holder.tvName.setText(friend.getName());
        holder.tvStatus.setEnabled(!friend.isFriend());

        if (friend.isFriend()) {
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
        RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailConsumer).doIfPresent(d -> d.accept(friend)));
        RxView.clicks(holder.tvStatus).subscribe(o -> Optional.from(inviteConsumer).doIfPresent(d -> d.accept(friend)));
    }

    @Override
    public int getItemCount() {
        return friends == null ? 0 : friends.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
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
