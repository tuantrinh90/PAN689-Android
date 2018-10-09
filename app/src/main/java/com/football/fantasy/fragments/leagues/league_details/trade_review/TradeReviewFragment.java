package com.football.fantasy.fragments.leagues.league_details.trade_review;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.StatePagerAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.league_details.trade_review.sub.SubTradeReviewFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TradeResponse;

import butterknife.BindView;
import butterknife.OnClick;

public class TradeReviewFragment extends BaseMvpFragment<ITradeReviewView, ITradeReviewPresenter<ITradeReviewView>> implements ITradeReviewView {
    static final String TAG = TradeReviewFragment.class.getSimpleName();

    static final String KEY_LEAGUE = "key_leagues";

    public static final int INDEX_REVIEWING = 0;
    public static final int INDEX_RESULTS = 1;

    @BindView(R.id.text_reviewing)
    ExtTextView textReviewing;
    @BindView(R.id.llReviewing)
    View llReviewing;
    @BindView(R.id.text_results)
    ExtTextView textResults;
    @BindView(R.id.llResults)
    View llResults;
    @BindView(R.id.view_pager)
    ViewPager viewPager;

    private int index;
    private int previewTradeId;

    public static TradeReviewFragment newInstance(LeagueResponse league) {

        Bundle args = new Bundle();
        args.putSerializable(KEY_LEAGUE, league);

        TradeReviewFragment fragment = new TradeReviewFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private static Bundle newBundle(LeagueResponse leagueResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        return bundle;
    }

    private LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_trade_review_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {
        buttonTeamSelected(true);

        StatePagerAdapter adapter = new StatePagerAdapter(getChildFragmentManager());
        adapter.addFragment(SubTradeReviewFragment.newInstance(TradeResponse.TYPE_REVIEWING, league, previewTradeId).setChildFragment(true));
        adapter.addFragment(SubTradeReviewFragment.newInstance(TradeResponse.TYPE_REVIEWED, league, previewTradeId).setChildFragment(true));

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // change here
                buttonTeamSelected(position == INDEX_REVIEWING);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if (index > 0) {
            viewPager.setCurrentItem(index);
        }
    }


    private void buttonTeamSelected(boolean selected) {
        llReviewing.setBackgroundResource(selected ? R.drawable.bg_draft_team_list_selected : R.drawable.bg_draft_team_list_normal);
        textReviewing.setTextColor(getResources().getColor(selected ? R.color.color_white : R.color.color_white_blue));

        llResults.setBackgroundResource(!selected ? R.drawable.bg_draft_team_list_selected : R.drawable.bg_draft_team_list_normal);
        textResults.setTextColor(getResources().getColor(!selected ? R.color.color_white : R.color.color_white_blue));
    }

    @NonNull
    @Override
    public ITradeReviewPresenter<ITradeReviewView> createPresenter() {
        return new TradeReviewPresenter(getAppComponent());
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    @OnClick({R.id.llReviewing, R.id.llResults})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llReviewing:
                viewPager.setCurrentItem(INDEX_REVIEWING);
                break;
            case R.id.llResults:
                viewPager.setCurrentItem(INDEX_RESULTS);
                break;
        }
    }

    public void openFragment(int index) {
        this.index = index;
        viewPager.setCurrentItem(index);
    }

    public void openTradeProposalReview(int tradeId) {
        previewTradeId = tradeId;
    }
}
