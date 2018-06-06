package com.football.fantasy.fragments.leagues.your_team;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.YourTeamViewPagerAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.fantasy.fragments.leagues.your_team.team_list.TeamListFragment;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;

import butterknife.BindView;

public class YourTeamFragment extends BaseMainMvpFragment<IYourTeamView, IYourTeamPresenter<IYourTeamView>> implements IYourTeamView {
    static final String KEY_LEAGUE_ID = "LEAGUE_ID";
    static final String KEY_LEAGUE = "LEAGUE";

    public static Bundle newBundle(int leagueId, LeagueResponse leagueResponse) {
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        return bundle;
    }

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    int leagueId;
    LeagueResponse leagueResponse;

    @Override
    public int getResourceId() {
        return R.layout.your_team_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
        leagueResponse = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
    }

    void initView() {
        cvCarouselView.setTextAllCaps(false)
                .setFontPath(getString(R.string.font_display_heavy_italic))
                .setAdapter(mActivity, new ArrayList<Carousel>() {{
                    add(new Carousel(getString(R.string.line_up), true));
                    add(new Carousel(getString(R.string.player_list), false));
                    add(new Carousel(getString(R.string.team_list), false));
                }}, R.color.color_blue, R.color.color_content, position -> {
                    cvCarouselView.setActivePosition(position);
                    vpViewPager.setCurrentItem(position);
                });

        vpViewPager.setAdapter(new YourTeamViewPagerAdapter(getFragmentManager(), new ArrayList<BaseMvpFragment>() {{
            add(LineUpFragment.newInstance().setChildFragment(true));
            add(PlayerListFragment.newInstance().setChildFragment(true));
            add(TeamListFragment.newInstance(leagueId, leagueResponse).setChildFragment(true));
        }}));
        vpViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cvCarouselView.setActivePosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public IYourTeamPresenter<IYourTeamView> createPresenter() {
        return new YourTeamPresenter(getAppComponent());
    }

    @Override
    public int getTitleId() {
        return R.string.league_details;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }
}