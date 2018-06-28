package com.football.adapters;

import android.animation.ObjectAnimator;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.customizes.expandablelayout.ExpandableLayoutListenerAdapter;
import com.football.customizes.expandablelayout.ExpandableLinearLayout;
import com.football.customizes.expandablelayout.Utils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;

import java.util.List;

import butterknife.BindView;

public class MatchupMyLeagueAdapter extends DefaultAdapter<String> {

    private SparseBooleanArray expandState = new SparseBooleanArray();


    public MatchupMyLeagueAdapter(List<String> dataSet) {
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
//        Context context = holder.itemView.getContext();
        holder.setIsRecyclable(false);
        holder.textView.setText(data);
        holder.expandableLayout.setInRecyclerView(true);
//        holder.expandableLayout.setInterpolator(data.interpolator);
        holder.expandableLayout.setExpanded(expandState.get(position));
        holder.expandableLayout.setListener(new ExpandableLayoutListenerAdapter() {
            @Override
            public void onPreOpen() {
            }

            @Override
            public void onPreClose() {
            }
        });

    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    static class MatchupMyLeagueHolder extends DefaultHolder {

        @BindView(R.id.button)
        RelativeLayout button;
        @BindView(R.id.textView)
        TextView textView;
        @BindView(R.id.expandableLayout)
        ExpandableLinearLayout expandableLayout;

        public MatchupMyLeagueHolder(View itemView) {
            super(itemView);
        }
    }
}
