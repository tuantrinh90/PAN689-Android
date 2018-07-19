package com.football.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.bon.image.ImageLoaderUtils;
import com.football.customizes.expandablelayout.ExpandableLayout;
import com.football.customizes.expandablelayout.util.Utils;
import com.football.customizes.images.CircleImageViewApp;
import com.football.customizes.match_up.MatchupTextItem;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.MyMatchResponse;
import com.football.models.responses.PlayerStatisticMetaResponse;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import java8.util.function.BiConsumer;

public class MatchupLeagueAdapter extends DefaultAdapter<MyMatchResponse> {

    private final BiConsumer<TeamResponse, LeagueResponse> teamDetailCallback;
    private SparseBooleanArray expandState = new SparseBooleanArray();

    public MatchupLeagueAdapter(BiConsumer<TeamResponse, LeagueResponse> teamDetailCallback) {
        super(new ArrayList<>());
        this.teamDetailCallback = teamDetailCallback;
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
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, MyMatchResponse data, int position) {
        MatchupMyLeagueHolder holder = (MatchupMyLeagueHolder) defaultHolder;
        Context context = holder.itemView.getContext();

        PlayerStatisticMetaResponse statisticTeamA = data.getTeam().getStatistic();
        PlayerStatisticMetaResponse statisticTeamB = data.getWithTeam().getStatistic();
        showStatistic(holder, statisticTeamA, statisticTeamB);
        holder.tvTitle.setText(data.getLeague().getName());

        ImageLoaderUtils.displayImage(data.getTeam().getLogo(), holder.ivAvatarTeam1.getImageView());
        holder.tvTitleTeam1.setText(data.getTeam().getName());

        ImageLoaderUtils.displayImage(data.getWithTeam().getLogo(), holder.ivAvatarTeam2.getImageView());
        holder.tvTitleTeam2.setText(data.getWithTeam().getName());

        // round
        holder.tvRound.setText(context.getString(R.string.round_number, data.getRound()));

        // round match
        holder.tvRoundMatch.setText(context.getString(R.string.round_match, data.getTeam().getPoint(), data.getWithTeam().getPoint()));

        // state
        if (expandState.get(position)) {
            holder.expandableView.expand();
            holder.ivExpandable.setRotation(180f);
        } else {
            holder.expandableView.collapse();
            holder.ivExpandable.setRotation(0f);
        }

        holder.ivExpandable.setOnClickListener(v -> {
            holder.expandableView.toggle();
            v.clearAnimation();
            if (holder.expandableView.isExpanded()) {
                createRotateAnimator(v, 0, 180f).start();
            } else {
                createRotateAnimator(v, 180f, 0f).start();
            }
            expandState.append(holder.getAdapterPosition(), !expandState.get(holder.getAdapterPosition()));
        });

        holder.ivAvatarTeam1.setOnClickListener(v -> {
            MyMatchResponse match = getItem(defaultHolder.getAdapterPosition());
            teamDetailCallback.accept(match.getTeam(), match.getLeague());
        });
        holder.ivAvatarTeam2.setOnClickListener(v -> {
            MyMatchResponse match = getItem(defaultHolder.getAdapterPosition());
            teamDetailCallback.accept(match.getWithTeam(), match.getLeague());
        });
    }

    private void showStatistic(MatchupMyLeagueHolder holder, PlayerStatisticMetaResponse statisticTeam, PlayerStatisticMetaResponse statisticWithTeam) {
        holder.matchupGoals.setTextLeft(String.valueOf(statisticTeam.getGoals()));
        holder.matchupAssists.setTextLeft(String.valueOf(statisticTeam.getAssists()));
        holder.matchupCleanSheet.setTextLeft(String.valueOf(statisticTeam.getCleanSheet()));
        holder.matchupDuelsTheyWin.setTextLeft(String.valueOf(statisticTeam.getDuelsTheyWin()));
        holder.matchupPasses.setTextLeft(String.valueOf(statisticTeam.getPasses()));
        holder.matchupShots.setTextLeft(String.valueOf(statisticTeam.getShots()));
        holder.matchupSaves.setTextLeft(String.valueOf(statisticTeam.getSaves()));
        holder.matchupYellowCard.setTextLeft(String.valueOf(statisticTeam.getYellowCards()));
        holder.matchupDribbles.setTextLeft(String.valueOf(statisticTeam.getDribbles()));
        holder.matchupTurnovers.setTextLeft(String.valueOf(statisticTeam.getTurnovers()));
        holder.matchupBallRecovered.setTextLeft(String.valueOf(statisticTeam.getBallsRecovered()));
        holder.matchupFoundCommitted.setTextLeft(String.valueOf(statisticTeam.getFoulsCommitted()));

        holder.matchupGoals.setTextRight(String.valueOf(statisticWithTeam.getGoals()));
        holder.matchupAssists.setTextRight(String.valueOf(statisticWithTeam.getAssists()));
        holder.matchupCleanSheet.setTextRight(String.valueOf(statisticWithTeam.getCleanSheet()));
        holder.matchupDuelsTheyWin.setTextRight(String.valueOf(statisticWithTeam.getDuelsTheyWin()));
        holder.matchupPasses.setTextRight(String.valueOf(statisticWithTeam.getPasses()));
        holder.matchupShots.setTextRight(String.valueOf(statisticWithTeam.getShots()));
        holder.matchupSaves.setTextRight(String.valueOf(statisticWithTeam.getSaves()));
        holder.matchupYellowCard.setTextRight(String.valueOf(statisticWithTeam.getYellowCards()));
        holder.matchupDribbles.setTextRight(String.valueOf(statisticWithTeam.getDribbles()));
        holder.matchupTurnovers.setTextRight(String.valueOf(statisticWithTeam.getTurnovers()));
        holder.matchupBallRecovered.setTextRight(String.valueOf(statisticWithTeam.getBallsRecovered()));
        holder.matchupFoundCommitted.setTextRight(String.valueOf(statisticWithTeam.getFoulsCommitted()));
    }

    public ObjectAnimator createRotateAnimator(final View target, final float from, final float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(Utils.createInterpolator(Utils.LINEAR_INTERPOLATOR));
        return animator;
    }

    @Override
    public void addItems(List<MyMatchResponse> items) {
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

        @BindView(R.id.matchupGoals)
        MatchupTextItem matchupGoals;
        @BindView(R.id.matchupAssists)
        MatchupTextItem matchupAssists;
        @BindView(R.id.matchupCleanSheet)
        MatchupTextItem matchupCleanSheet;
        @BindView(R.id.matchupDuelsTheyWin)
        MatchupTextItem matchupDuelsTheyWin;
        @BindView(R.id.matchupPasses)
        MatchupTextItem matchupPasses;
        @BindView(R.id.matchupShots)
        MatchupTextItem matchupShots;
        @BindView(R.id.matchupSaves)
        MatchupTextItem matchupSaves;
        @BindView(R.id.matchupYellowCard)
        MatchupTextItem matchupYellowCard;
        @BindView(R.id.matchupDribbles)
        MatchupTextItem matchupDribbles;
        @BindView(R.id.matchupTurnovers)
        MatchupTextItem matchupTurnovers;
        @BindView(R.id.matchupBallRecovered)
        MatchupTextItem matchupBallRecovered;
        @BindView(R.id.matchupFoundCommitted)
        MatchupTextItem matchupFoundCommitted;

        public MatchupMyLeagueHolder(View itemView) {
            super(itemView);
        }
    }
}
