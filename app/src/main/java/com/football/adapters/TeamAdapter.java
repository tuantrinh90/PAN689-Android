package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.Team;
import com.jakewharton.rxbinding2.view.RxView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class TeamAdapter extends RecyclerView.Adapter<TeamAdapter.ViewHolder> {
    Context context;
    List<Team> teams;
    Consumer<Team> detailConsumer;
    Consumer<Team> teamConsumer;

    public TeamAdapter(Context context, List<Team> teams, Consumer<Team> detailConsumer, Consumer<Team> teamConsumer) {
        this.context = context;
        this.teams = teams;
        this.detailConsumer = detailConsumer;
        this.teamConsumer = teamConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.team_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemCount() <= 0) return;

        Team team = teams.get(position);
        holder.ivAvatar.setImageUri(team.getAvatar());
        holder.tvTeam.setText(team.getTeamName());
        holder.tvDescription.setText(team.getDescription());
        holder.tvRemove.setVisibility(team.getIsLock() ? View.GONE : View.VISIBLE);
        holder.ivLock.setVisibility(team.getIsLock() ? View.VISIBLE : View.GONE);

        // click
        RxView.clicks(holder.itemView).subscribe(o -> Optional.from(detailConsumer).doIfPresent(t -> t.accept(team)));
        RxView.clicks(holder.tvRemove).subscribe(o -> Optional.from(teamConsumer).doIfPresent(t -> t.accept(team)));
    }

    @Override
    public int getItemCount() {
        return teams == null ? 0 : teams.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;
        @BindView(R.id.tvRemove)
        ExtTextView tvRemove;
        @BindView(R.id.ivLock)
        AppCompatImageView ivLock;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
