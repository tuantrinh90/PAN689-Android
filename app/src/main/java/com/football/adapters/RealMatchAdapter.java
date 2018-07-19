package com.football.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.bon.customview.listview.ExtBaseAdapter;
import com.bon.customview.listview.ExtListView;
import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.DefaultHolder;
import com.football.fantasy.R;
import com.football.models.responses.RealMatch;
import com.football.models.responses.RealMatchResponse;
import com.football.utilities.Constant;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RealMatchAdapter extends DefaultAdapter<RealMatch> {

    public RealMatchAdapter(Context context) {
        super(context);
    }

    @Override
    protected int getLayoutId(int viewType) {
        return R.layout.match_up_real_match_item;
    }

    @Override
    protected DefaultHolder onCreateHolder(View v, int viewType) {
        return new RealMatchHolder(v);
    }

    @Override
    protected void onBindViewHolder(@NonNull DefaultHolder defaultHolder, RealMatch data, int position) {
        RealMatchHolder holder = (RealMatchHolder) defaultHolder;
        holder.tvTitle.setText(DateTimeUtils.convertCalendarToString(DateTimeUtils.convertStringToCalendar(data.getDate(), Constant.FORMAT_DATE), Constant.FORMAT_DAY_OF_WEEK));
        holder.tvDate.setText("(" + data.getDate() + ")");
        LeagueResultAdapter adapter = new LeagueResultAdapter(holder.itemView.getContext(), data.getResponses());
        holder.rvResult.setAdapter(adapter);
    }

    public static class RealMatchHolder extends DefaultHolder {

        @BindView(R.id.tvTitle)
        ExtTextView tvTitle;
        @BindView(R.id.tvDate)
        ExtTextView tvDate;
        @BindView(R.id.rv_result)
        ExtListView rvResult;

        public RealMatchHolder(View itemView) {
            super(itemView);
        }
    }

    public class LeagueResultAdapter extends ExtBaseAdapter<RealMatchResponse, LeagueResultAdapter.LeagueResultHolder> {

        public LeagueResultAdapter(Context ctx, List<RealMatchResponse> its) {
            super(ctx, its);
        }

        @Override
        protected int getViewId() {
            return R.layout.match_up_real_match_league_item;
        }

        @Override
        protected LeagueResultHolder onCreateViewHolder(View view) {
            return new LeagueResultHolder(view);
        }

        @Override
        protected void onBindViewHolder(LeagueResultHolder holder, RealMatchResponse data) {
            holder.tvTeam1.setText(data.getTeam1());
            holder.tvTeam2.setText(data.getTeam2());
            holder.tvMatchTeam1.setText(String.valueOf(data.getTeam1Result()));
            holder.tvMatchTeam2.setText(String.valueOf(data.getTeam2Result()));

            holder.tvTime.setText(DateTimeUtils.convertCalendarToString(DateTimeUtils.convertStringToCalendar(data.getEndAt(), Constant.FORMAT_DATE_TIME_SERVER), Constant.FORMAT_HOUR_MINUTE));
        }

        class LeagueResultHolder extends ExtPagingListView.ExtViewHolder {

            @BindView(R.id.tvTime)
            ExtTextView tvTime;
            @BindView(R.id.tvTeam1)
            ExtTextView tvTeam1;
            @BindView(R.id.tvTeam2)
            ExtTextView tvTeam2;
            @BindView(R.id.tvMatchTeam1)
            ExtTextView tvMatchTeam1;
            @BindView(R.id.tvMatchTeam2)
            ExtTextView tvMatchTeam2;

            public LeagueResultHolder(View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
