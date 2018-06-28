package com.football.fantasy.fragments.match_up;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.football.adapters.LeagueViewPagerAdapter;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.match_up.my.MatchupMyLeagueFragment;
import com.football.fantasy.fragments.match_up.real.MatchupRealLeagueFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class MatchUpFragment extends BaseMainMvpFragment<IMatchUpView, IMatchUpPresenter<IMatchUpView>> implements IMatchUpView {
    public static MatchUpFragment newInstance() {
        return new MatchUpFragment();
    }

    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    private boolean initialized = false;

    private LeagueViewPagerAdapter leagueViewPagerAdapter;

    @Override
    public int getResourceId() {
        return R.layout.match_up_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
    }

    @NonNull
    @Override
    public IMatchUpPresenter<IMatchUpView> createPresenter() {
        return new MatchUpDataPresenter(getAppComponent());
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && !initialized) {
            initialized = true;
            initView();
        }
    }

    void initView() {
        // carousel
        cvCarouselView.setAdapter(mActivity, new ArrayList<Carousel>() {{
            add(new Carousel(getString(R.string.real_league), true));
            add(new Carousel(getString(R.string.my_league), false));
        }}, R.color.color_blue, R.color.color_white, position -> {
            cvCarouselView.setActivePosition(position);
            vpViewPager.setCurrentItem(position);
        });

        // view pager
        leagueViewPagerAdapter = new LeagueViewPagerAdapter(getFragmentManager(), new ArrayList<BaseMvpFragment>() {{
            add(MatchupRealLeagueFragment.newInstance());
            add(MatchupMyLeagueFragment.newInstance());
        }});
        vpViewPager.setOffscreenPageLimit(2);
        vpViewPager.setAdapter(leagueViewPagerAdapter);
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

}
