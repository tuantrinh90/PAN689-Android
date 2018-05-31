package com.football.adapters;

import android.content.Context;
import android.view.View;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.radiobutton.ExtRadioButton;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.customizes.images.CircleImageViewApp;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import java8.util.function.Consumer;

public class SuccessorAdapter extends ExtBaseAdapter<TeamResponse, SuccessorAdapter.ViewHolder> {
    Consumer<TeamResponse> teamConsumer;

    public SuccessorAdapter(Context context, List<TeamResponse> teamResponses, Consumer<TeamResponse> teamConsumer) {
        super(context, teamResponses);
        this.teamConsumer = teamConsumer;
    }

    @Override
    protected int getViewId() {
        return R.layout.successor_item;
    }

    @Override
    protected ViewHolder onCreateViewHolder(View view) {
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(ViewHolder holder, TeamResponse teamResponse) {
        holder.rbRadioButton.setChecked(teamResponse.isChecked());
        holder.ivAvatar.setImageUri(teamResponse.getLogo());
        holder.tvTeam.setText(teamResponse.getName());
        holder.tvDescription.setText(teamResponse.getDescription());
        holder.itemView.setOnClickListener(v -> Optional.from(teamConsumer).doIfPresent(t -> t.accept(teamResponse)));
    }

    class ViewHolder extends ExtPagingListView.ExtViewHolder {
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
