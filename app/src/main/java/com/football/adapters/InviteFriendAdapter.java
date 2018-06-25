package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.FriendResponse;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class InviteFriendAdapter extends DefaultAdapter<FriendResponse> {

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private Consumer<FriendResponse> detailCallback;
    private Consumer<FriendResponse> inviteCallback;

    private boolean startTime;

    public InviteFriendAdapter(List<FriendResponse> friends,
                               Consumer<FriendResponse> detailCallback,
                               Consumer<FriendResponse> inviteCallback,
                               boolean startTime) {
        super(friends);
        this.detailCallback = detailCallback;
        this.inviteCallback = inviteCallback;
        this.startTime = startTime;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.invite_friend_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new ViewHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, FriendResponse friend, int position) {
        ViewHolder holder = (ViewHolder) defaultHolder;
        Context context = holder.itemView.getContext();

        holder.ivAvatar.setImageUri(friend.getPhoto());
        holder.tvName.setText(friend.getName());
        holder.tvStatus.setEnabled(!friend.getInvited());

        if (startTime) {
            holder.tvStatus.setText(R.string.invite);
            holder.tvStatus.setEnabled(false);
            holder.tvStatus.setBackgroundResource(R.drawable.bg_gray_border_gray_radius);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_label));

        } else if (friend.getInvited()) {
            holder.tvStatus.setBackground(null);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_content));
            holder.tvStatus.setEnabled(false);
            holder.tvStatus.setText(R.string.invited);
        } else {
            holder.tvStatus.setBackgroundResource(R.drawable.bg_blue_radius_selector);
            holder.tvStatus.setEnabled(true);
            holder.tvStatus.setText(R.string.invite);
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.color_white));
        }

        // click
        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailCallback).doIfPresent(d -> d.accept(friend))));
        mDisposable.add(RxView.clicks(holder.tvStatus).subscribe(o -> Optional.from(inviteCallback).doIfPresent(d -> d.accept(friend))));
    }

    static class ViewHolder extends DefaultHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvName)
        ExtTextView tvName;
        @BindView(R.id.tvStatus)
        ExtTextView tvStatus;

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
