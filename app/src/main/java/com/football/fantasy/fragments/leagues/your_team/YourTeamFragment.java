package com.football.fantasy.fragments.leagues.your_team;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.football.adapters.YourTeamViewPagerAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.events.LineupEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.your_team.draft_teamlist.DraftTeamListFragment;
import com.football.fantasy.fragments.leagues.your_team.line_up.LineUpFragment;
import com.football.fantasy.fragments.leagues.your_team.player_list.PlayerListFragment;
import com.football.fantasy.fragments.leagues.your_team.team_list.TeamListFragment;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;
import java.util.Objects;

import butterknife.BindView;
import io.reactivex.observers.DisposableObserver;

public class YourTeamFragment extends BaseMvpFragment<IYourTeamView, IYourTeamPresenter<IYourTeamView>> implements IYourTeamView {
    static final String KEY_LEAGUE = "LEAGUE";

    public static Bundle newBundle(LeagueResponse leagueResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        return bundle;
    }

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    LeagueResponse league;

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
        registerEvent();
    }

    private void getDataFromBundle() {
        Bundle bundle = getArguments();
        league = (LeagueResponse) bundle.getSerializable(KEY_LEAGUE);
    }

    void initView() {
        cvCarouselView.setTextAllCaps(false)
                .setFontPath(getString(R.string.font_display_heavy_italic))
                .setAdapter(mActivity, new ArrayList<Carousel>() {{
                    add(new Carousel(getString(R.string.lineup), true));
                    add(new Carousel(getString(R.string.player_list), false));
                    add(new Carousel(getString(R.string.team_list), false));
                }}, R.color.color_blue, R.color.color_content, position -> {
                    cvCarouselView.setActivePosition(position);
                    vpViewPager.setCurrentItem(position);
                });

        vpViewPager.setAdapter(new YourTeamViewPagerAdapter(getFragmentManager(), new ArrayList<BaseMvpFragment>() {{
            add(LineUpFragment.newInstance(league,
                    league.getTeam() == null ? 0 : league.getTeam().getId()
            ).setChildFragment(true));
            add(PlayerListFragment.newInstance(league).setChildFragment(true));
//            add(TeamListFragment.newInstance(league).setChildFragment(true));
            add(DraftTeamListFragment.newInstance(league).setChildFragment(true));
        }}));
        vpViewPager.setOffscreenPageLimit(3);
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

    private void registerEvent() {
        // complete lineup event
        mCompositeDisposable.add(bus.ofType(LineupEvent.class)
                .subscribeWith(new DisposableObserver<LineupEvent>() {
                    @Override
                    public void onNext(LineupEvent event) {
                        try {
                            if (((YourTeamViewPagerAdapter) Objects.requireNonNull(vpViewPager.getAdapter())).getItem(2) instanceof TeamListFragment) {
                                vpViewPager.setCurrentItem(2);
                            }
                        } catch (NullPointerException e) {

                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                }));
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
