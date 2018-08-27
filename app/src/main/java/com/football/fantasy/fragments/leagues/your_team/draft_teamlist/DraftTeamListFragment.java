package com.football.fantasy.fragments.leagues.your_team.draft_teamlist;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.YourTeamViewPagerAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_picks.DraftPicksFragment;
import com.football.fantasy.fragments.leagues.your_team.draft_teamlist.draft_teams.DraftTeamsFragment;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class DraftTeamListFragment extends BaseMvpFragment<IDraftTeamListView, IDraftTeamListPresenter<IDraftTeamListView>> implements IDraftTeamListView {
    private static final String TAG = DraftTeamListFragment.class.getSimpleName();

    private static final String KEY_LEAGUE = "LEAGUE";
    private static final int INDEX_TEAM = 0;
    private static final int INDEX_PICKS = 1;

    @BindView(R.id.view_pager)
    ViewPager viewPager;
    @BindView(R.id.text_team)
    ExtTextView textTeam;
    @BindView(R.id.llTeam)
    LinearLayout llTeam;
    @BindView(R.id.text_picks)
    ExtTextView textPicks;
    @BindView(R.id.llPicks)
    LinearLayout llPicks;

    private LeagueResponse league;

    public static DraftTeamListFragment newInstance(LeagueResponse league) {
        DraftTeamListFragment fragment = new DraftTeamListFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, league);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getResourceId() {
        return R.layout.draft_team_list_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        getDataFromBundle();
        initView();
        registerBus();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        if (bundle != null) {
            league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
        }
    }

    void initView() {
        buttonTeamSelected(true);

        viewPager.setAdapter(new YourTeamViewPagerAdapter(getFragmentManager(), new ArrayList<BaseMvpFragment>() {{
            add(DraftTeamsFragment.newInstance(league).setChildFragment(true));
            add(DraftPicksFragment.newInstance(league).setChildFragment(true));
        }}));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // change here
                buttonTeamSelected(position == INDEX_TEAM);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @NonNull
    @Override
    public IDraftTeamListPresenter<IDraftTeamListView> createPresenter() {
        return new DraftTeamListPresenter(getAppComponent());
    }

    private void registerBus() {
    }

    @OnClick({R.id.llTeam, R.id.llPicks})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llTeam:
                viewPager.setCurrentItem(INDEX_TEAM);

                break;
            case R.id.llPicks:
                viewPager.setCurrentItem(INDEX_PICKS);
                break;
        }
    }

    private void buttonTeamSelected(boolean selected) {
        llTeam.setBackgroundResource(selected ? R.drawable.bg_draft_team_list_selected : R.drawable.bg_draft_team_list_normal);
        textTeam.setTextColor(getResources().getColor(selected ? R.color.color_white : R.color.color_white_blue));

        llPicks.setBackgroundResource(!selected ? R.drawable.bg_draft_team_list_selected : R.drawable.bg_draft_team_list_normal);
        textPicks.setTextColor(getResources().getColor(!selected ? R.color.color_white : R.color.color_white_blue));
    }

}
