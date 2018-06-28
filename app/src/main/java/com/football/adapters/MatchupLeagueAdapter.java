package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.customizes.expandablelayout.ExpandableLayout;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;

public class MatchupLeagueAdapter extends DefaultAdapter<String> {

    private SparseBooleanArray expandState = new SparseBooleanArray();


    public MatchupLeagueAdapter(List<String> dataSet) {
        super(dataSet);
        for (int i = 0; i < dataSet.size(); i++) {
            expandState.append(i, false);
        }
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.match_up_my_league_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new MatchupMyLeagueHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, String data, int position) {
        MatchupMyLeagueHolder holder = (MatchupMyLeagueHolder) defaultHolder;
        Context context = holder.itemView.getContext();
        if (expandState.get(position)) {
            holder.expandableView.expand();
        } else {
            holder.expandableView.collapse();
        }
        holder.ivExpandable.setOnClickListener(v -> {
            holder.expandableView.toggle();
            expandState.append(holder.getAdapterPosition(), !expandState.get(holder.getAdapterPosition()));
        });
    }

    @Override
    public void addItems(List<String> items) {
        int index = getItemCount();
        super.addItems(items);
        for (int i = 0, size = items.size(); i < size; i++) {
            expandState.append(index, false);
            index++;
        }
    }

    static class MatchupMyLeagueHolder extends DefaultHolder {

        @BindView(R.id.tvTitle)
        ExtTextView tvTitle;
        @BindView(R.id.buttonResult)
        LinearLayout buttonResult;
        @BindView(R.id.ivAvatarTeam1)
        CircleImageViewApp ivAvatarTeam1;
        @BindView(R.id.tvTitleTeam1)
        ExtTextView tvTitleTeam1;
        @BindView(R.id.tvRound)
        ExtTextView tvRound;
        @BindView(R.id.tvRoundMatch)
        ExtTextView tvRoundMatch;
        @BindView(R.id.ivAvatarTeam2)
        CircleImageViewApp ivAvatarTeam2;
        @BindView(R.id.tvTitleTeam2)
        ExtTextView tvTitleTeam2;
        @BindView(R.id.ivExpandable)
        ImageView ivExpandable;
        @BindView(R.id.expandableView)
        ExpandableLayout expandableView;

        public MatchupMyLeagueHolder(View itemView) {
            super(itemView);
        }
    }
}
