package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.radiobutton.ExtRadioButton;
import com.bon.customview.textview.ExtTextView;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;

import butterknife.BindView;
import java8.util.function.Consumer;

public class TeamSelectAdapter extends DefaultAdapter<TeamResponse> {

    private Consumer<Void> clickConsumer;
    private int lastSelected = -1;

    public TeamSelectAdapter(Context context, Consumer<Void> clickConsumer) {
        super(context);
        this.clickConsumer = clickConsumer;
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.team_select_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new TeamSelectHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, TeamResponse data, int position) {
        TeamSelectHolder holder = (TeamSelectHolder) defaultHolder;

        holder.rbRadioButton.setChecked(data.isChecked());
        holder.ivAvatar.setImageUri(data.getLogo());
        holder.tvTeam.setText(data.getName());
        holder.tvDescription.setText(data.getName());
        holder.itemView.setOnClickListener(v -> {
            if (lastSelected != position) {
                if (lastSelected != -1) {
                    getItem(lastSelected).setChecked(false);
                    notifyItemChanged(lastSelected);
                }
                lastSelected = position;
                getItem(lastSelected).setChecked(true);
                notifyItemChanged(lastSelected);
                clickConsumer.accept(null);
            }
        });
    }

    public TeamResponse getTeamSelected() {
        if (lastSelected != -1) {
            return getItem(lastSelected);
        }
        return null;
    }

    class TeamSelectHolder extends DefaultHolder {
        @BindView(R.id.rbRadioButton)
        ExtRadioButton rbRadioButton;
        @BindView(R.id.ivAvatar)
        CircleImageViewApp ivAvatar;
        @BindView(R.id.tvTeam)
        ExtTextView tvTeam;
        @BindView(R.id.tvDescription)
        ExtTextView tvDescription;

        public TeamSelectHolder(View itemView) {
            super(itemView);
        }
    }
}
