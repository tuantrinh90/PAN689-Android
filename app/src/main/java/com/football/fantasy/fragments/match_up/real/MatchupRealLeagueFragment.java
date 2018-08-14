package com.football.fantasy.fragments.match_up.real;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.RealMatchAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.DefaultAdapter;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.RealMatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.utilities.Constant.ROUND_DEFAULT;

public class MatchupRealLeagueFragment extends BaseMainMvpFragment<IMatchupRealLeagueView, IMatchupRealLeaguePresenter<IMatchupRealLeagueView>> implements IMatchupRealLeagueView {

    private static final String VALUE_ALL_ROUND = "ALL ROUND";

    @BindView(R.id.rvRealLeague)
    ExtRecyclerView<RealMatch> rvRealLeague;
    @BindView(R.id.tvRound)
    ExtTextView tvRound;

    private List<ExtKeyValuePair> valuePairs;
    private String round = ROUND_DEFAULT;
    private int page;

    public static MatchupRealLeagueFragment newInstance() {
        return new MatchupRealLeagueFragment();
    }

    @Override
    public int getResourceId() {
        return R.layout.match_up_real_league_fragment;
    }

    @Override
    protected void initialized() {
        super.initialized();
        initData();
        initView();

        page = 1;
        getRealMatches();
        rvRealLeague.startLoading();
    }

    private void initData() {
        valuePairs = new ArrayList<>();
        valuePairs.add(new ExtKeyValuePair(ROUND_DEFAULT, VALUE_ALL_ROUND));
        for (int i = 0; i < 30; i++) {
            valuePairs.add(new ExtKeyValuePair(String.valueOf(i + 1), "Round " + (i + 1)));
        }
    }

    private void initView() {
        tvRound.setText(VALUE_ALL_ROUND);
        RealMatchAdapter adapter = new RealMatchAdapter(getContext());
        rvRealLeague
                .adapter(adapter)
                .hasFixedSize(false)
                .refreshListener(this::refresh)
                .loadMoreListener(() -> {
                    page++;
                    getRealMatches();
                })
                .build();
    }

    private void refresh() {
        page = 1;
        rvRealLeague.clear();
        getRealMatches();
    }

    private void getRealMatches() {
        presenter.getRealMatches(round, page);
    }

    @NonNull
    @Override
    public IMatchupRealLeaguePresenter<IMatchupRealLeagueView> createPresenter() {
        return new MatchupRealDataLeaguePresenter(getAppComponent());
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        if (!isLoading) {
            rvRealLeague.stopLoading();
        }
    }

    @OnClick(R.id.round)
    public void onRoundClicked() {
        ExtKeyValuePairDialogFragment.newInstance()
                .title(getString(R.string.select_round))
                .setValue(round)
                .setExtKeyValuePairs(valuePairs)
                .setOnSelectedConsumer(extKeyValuePair -> {
                    if (!TextUtils.isEmpty(extKeyValuePair.getKey())) {
                        tvRound.setText(extKeyValuePair.getValue());
                        round = extKeyValuePair.getKey();
                        refresh();
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void displayRealMatches(List<RealMatch> realMatches) {
        DefaultAdapter<RealMatch> adapter = rvRealLeague.getAdapter();
        int lastIndex = adapter.getItemCount() - 2; // trừ 2 vì lastItem là loadMore
        if (realMatches.size() > 0 && lastIndex >= 0 && adapter.getDataSet().get(lastIndex).getDate().equals(realMatches.get(0).getDate())) {
            RealMatch firstRealMatch = realMatches.get(0);
            realMatches.remove(0);

            // add firstRealMatch of response to lastRealMatch of Adapter
            adapter.getItem(lastIndex).addRealMatchResponse(firstRealMatch.getResponses());

            // add to rv
            rvRealLeague.addItems(realMatches);
            rvRealLeague.getAdapter().notifyItemChanged(lastIndex);
        } else {
            rvRealLeague.addItems(realMatches);
        }
    }

    @Override
    public void displayRound(Integer round) {
        this.round = String.valueOf(round);
        tvRound.setText(valuePairs.get(round).getValue());
    }

    @Override
    public void stopLoading() {
        rvRealLeague.stopLoading();
    }
}
