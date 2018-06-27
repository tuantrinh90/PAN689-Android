package com.football.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import io.reactivex.disposables.CompositeDisposable;
import java8.util.function.Consumer;

public class TeamAdapter extends DefaultAdapter<TeamResponse> {

    private CompositeDisposable mDisposable = new CompositeDisposable();

    private boolean leagueOwner;
    private Consumer<TeamResponse> detailCallback;
    private Consumer<TeamResponse> removeCallback;

    public TeamAdapter(List<TeamResponse> teams,
                       boolean leagueOwner,
                       Consumer<TeamResponse> detailCallback,
                       Consumer<TeamResponse> removeCallback) {
        super(teams);
        this.leagueOwner = leagueOwner;
        this.detailCallback = detailCallback;
        this.removeCallback = removeCallback;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new TeamHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TeamResponse data, int position) {

        TeamHolder holder = (TeamHolder) defaultHolder;

        holder.ivAvatar.setImageUri(data.getLogo());
        holder.tvTeam.setText(data.getName());
        holder.tvOwner.setText(data.getUser() == null ? "" : AppUtilities.getNameOrMe(holder.itemView.getContext(), data));

        if (data.getCompleted()) {
            holder.ivLock.setVisibility(View.GONE);
            holder.tvRemove.setVisibility(View.GONE);
            holder.tvCompleted.setVisibility(View.VISIBLE);
        } else {
            boolean owner = data.getOwner();
            holder.tvRemove.setVisibility(this.leagueOwner && !owner ? View.VISIBLE : View.GONE);
            holder.ivLock.setVisibility(owner ? View.VISIBLE : View.GONE);
            holder.tvCompleted.setVisibility(View.GONE);
        }

        // click
        mDisposable.add(RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailCallback).doIfPresent(t -> {
            if (data.getCompleted()) {
                t.accept(data);
            }
        })));
        mDisposable.add(RxView.clicks(holder.tvRemove).subscribe(o -> Optional.from(removeCallback).doIfPresent(t -> t.accept(data))));
    }

    static class TeamHolder extends DefaultHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvOwner)
        ExtTextView tvOwner;
        @BindView(R.id.tvRemove)
        ExtTextView tvRemove;
        @BindView(R.id.tvCompleted)
        ExtTextView tvCompleted;
        @BindView(R.id.ivLock)
        ImageView ivLock;

        public TeamHolder(View itemView) {
            super(itemView);
        }
    }
}
