package com.football.fantasy.fragments.match_up.real;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.football.adapters.RealMatchAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.recyclerview.ExtRecyclerView;
import com.football.fantasy.R;
import com.football.models.responses.RealMatch;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MatchupRealLeagueFragment extends BaseMainMvpFragment<IMatchupRealLeagueView, IMatchupRealLeaguePresenter<IMatchupRealLeagueView>> implements IMatchupRealLeagueView {

    public static MatchupRealLeagueFragment newInstance() {
        return new MatchupRealLeagueFragment();
    }

    @BindView(R.id.rvRealLeague)
    ExtRecyclerView<RealMatch> rvRealLeague;
    @BindView(R.id.tvRound)
    ExtTextView tvRound;

    private List<ExtKeyValuePair> valuePairs;
    private String round = "";
    private int page = 1;

    @Override
    public int getResourceId() {
        return R.layout.match_up_real_league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initData();
        initView();

        getRealMatches();
    }

    private void initData() {
        valuePairs = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            valuePairs.add(new ExtKeyValuePair(String.valueOf(i + 1), String.valueOf(i + 1)));
        }
    }

    private void initView() {
        RealMatchAdapter adapter = new RealMatchAdapter();
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

    @OnClick(R.id.round)
    public void onRoundClicked() {
        ExtKeyValuePairDialogFragment.newInstance()
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
        rvRealLeague.addItems(realMatches);
    }
}
