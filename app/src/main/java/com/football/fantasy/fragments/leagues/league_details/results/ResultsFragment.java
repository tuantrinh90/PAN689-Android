package com.football.fantasy.fragments.leagues.league_details.results;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.ResultsAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.TeamDetailFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.MatchResponse;
import com.football.utilities.SocketEventKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.utilities.Constant.ROUND_DEFAULT;

public class ResultsFragment extends BaseMvpFragment<IResultsView, IResultsPresenter<IResultsView>> implements IResultsView {
    static final String TAG = ResultsFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";

    @BindView(R.id.rv_results)
    ExtRecyclerView<MatchResponse> rvResults;
    @BindView(R.id.view_time)
    View viewTime;
    @BindView(R.id.text_time)
    ExtTextView textTime;
    @BindView(R.id.tvRound)
    ExtTextView tvRound;

    private String round = "";
    private List<ExtKeyValuePair> valuePairs;
    private int displayRound = -1; // save round when open this fragment from MatchupRealLeague

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

        registerSocket();
    }

    @Override
    public void onDestroyView() {
        getAppContext().off(SocketEventKey.EVENT_MATCH_RESULTS);
        super.onDestroyView();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    private void initData() {
        valuePairs = new ArrayList<>();
    }

    void initView() {
        tvRound.setText(getString(R.string.all_rounds));
        ResultsAdapter adapter = new ResultsAdapter(
                getContext(),
                (team) -> {
                    AloneFragmentActivity.with(this)
                            .parameters(TeamDetailFragment.newBundle(getString(R.string.results), team.getId(), this.league))
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

    private void registerSocket() {
        getAppContext().getSocket().on(SocketEventKey.EVENT_MATCH_RESULTS, args -> {
            Log.d(SocketEventKey.EVENT_MATCH_RESULTS, "registerSocket: ");
            mActivity.runOnUiThread(this::refresh);
        });
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
        rvResults.addItems(matches);
        // display time
        if (rvResults.getAdapter().getItemCount() > 0) {
            textTime.setText(rvResults.getItem(0).getStartAtFormatted());

            viewTime.setVisibility(View.VISIBLE);
        } else {
            viewTime.setVisibility(View.GONE);
        }
    }

    @Override
    public void displayRound(int round) {
        this.displayRound = round;

        if (valuePairs.size() > displayRound) {
            this.round = String.valueOf(displayRound);
            tvRound.setText(valuePairs.get(displayRound).getValue());
        }
    }

    @Override
    public void displayTotalRound(int totalRound) {
        valuePairs.clear();
        valuePairs.add(new ExtKeyValuePair(ROUND_DEFAULT, getString(R.string.all_rounds)));
        for (int i = 0; i < totalRound; i++) {
            valuePairs.add(new ExtKeyValuePair(String.valueOf(i + 1), "Round " + (i + 1)));
        }

        if (displayRound != -1) {
            this.round = String.valueOf(displayRound);
            tvRound.setText(valuePairs.get(displayRound).getValue());
        }
    }
}
