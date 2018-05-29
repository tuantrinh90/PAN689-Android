package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bon.customview.radiobutton.ExtRadioButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.common.adapters.BaseRecyclerViewAdapter;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.Team;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class SuccessorAdapter extends BaseRecyclerViewAdapter<Team, SuccessorAdapter.ViewHolder> {
    Consumer<Team> teamConsumer;

    public SuccessorAdapter(Context context, List<Team> teams, Consumer<Team> teamConsumer) {
        super(context, teams);
        this.teamConsumer = teamConsumer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(layoutInflater.inflate(R.layout.successor_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Team team = getItem(position);
        assert team != null;

        holder.rbRadioButton.setChecked(team.isChecked());
        holder.ivAvatar.setImageUri(team.getAvatar());
        holder.tvTeam.setText(team.getTeamName());
        holder.tvDescription.setText(team.getDescription());
        holder.itemView.setOnClickListener(v -> Optional.from(teamConsumer).doIfPresent(t -> t.accept(team)));
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rbRadioButton)
        ExtRadioButton rbRadioButton;
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
