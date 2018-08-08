package com.football.fantasy.fragments.leagues.league_details.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.DateTimeUtils;
import com.football.adapters.ResultsAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.MatchResponse;
import com.football.utilities.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.utilities.Constant.ROUND_DEFAULT;

public class ResultsFragment extends BaseMvpFragment<IResultsView, IResultsPresenter<IResultsView>> implements IResultsView {
    static final String TAG = ResultsFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";
    private static final String ALL_ROUND = "All round";

    @BindView(R.id.rv_results)
    ExtRecyclerView<MatchResponse> rvResults;
    @BindView(R.id.text_time)
    ExtTextView textTime;
    @BindView(R.id.tvRound)
    ExtTextView tvRound;

    private String round = ROUND_DEFAULT;
    private List<ExtKeyValuePair> valuePairs;

    public static ResultsFragment newInstance(LeagueResponse leagueResponse) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_LEAGUE, leagueResponse);

        ResultsFragment fragment = new ResultsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_results_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initData();
        initView();

        getResults();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    private void initData() {
        valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair(ROUND_DEFAULT, ALL_ROUND));
        for (int i = 0; i < 30; i++) {
            valuePairs.add(new ExtKeyValuePair(String.valueOf(i + 1), "Round " + (i + 1)));
        }
    }

    void initView() {
        tvRound.setText(ALL_ROUND);
        ResultsAdapter adapter = new ResultsAdapter(
                getContext(),
                (team, league) -> {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(team.getId(), league))
                            .start(TeamDetailFragment.class);
                });
        rvResults
                .adapter(adapter)
                .refreshListener(this::refresh)
                .build();
    }

    private void getResults() {
        presenter.getMatchResults(league.getId(), round);
    }

    private void refresh() {
        rvResults.clear();
        getResults();
    }

    @NonNull
    @Override
    public IResultsPresenter<IResultsView> createPresenter() {
        return new ResultsPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    @OnClick(R.id.round)
    public void onRoundClicked() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setValue(round)
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        round = extKeyValuePair.getKey();
                        tvRound.setText(extKeyValuePair.getValue());
                        refresh();
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvResults.stopLoading();
        }
    }

    @Override
    public void displayMatches(List<MatchResponse> matches) {
        // display time
        if (rvResults.getAdapter().getItemCount() > 0) {
            textTime.setText(DateTimeUtils.convertCalendarToString(
                    DateTimeUtils.convertStringToCalendar(
                            rvResults.getItem(0).getStartAt(),
                            Constant.FORMAT_DATE_TIME_SERVER),
                    Constant.FORMAT_DATE_TIME));
        }

        rvResults.addItems(matches);
    }
}
