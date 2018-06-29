package com.football.fantasy.fragments.leagues;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.bon.logger.Logger;
import com.football.adapters.LeagueViewPagerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_leagues.SetUpLeagueFragment;
import com.football.fantasy.fragments.leagues.league_details.LeagueDetailFragment;
import com.football.fantasy.fragments.leagues.my_leagues.MyLeagueFragment;
import com.football.fantasy.fragments.leagues.open_leagues.OpenLeagueFragment;
import com.football.fantasy.fragments.leagues.pending_invitation.PendingInvitationFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueFragment extends BaseMainMvpFragment<ILeagueView, ILeaguePresenter<ILeagueView>> implements ILeagueView {
    static final String TAG = LeagueFragment.class.getSimpleName();

    private static final int OPEN_LEAGUE = 0;
    private static final int MY_LEAGUE = 1;
    private static final int PENDING_INVITATION = 2;

    public static LeagueFragment newInstance() {
        return new LeagueFragment();
    }

    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    private boolean initialized = false;
    LeagueViewPagerAdapter leagueViewPagerAdapter;

    @Override
    public int getResourceId() {
        return R.layout.league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
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
        try {
            // carousel
            cvCarouselView.setAdapter(mActivity, new ArrayList<Carousel>() {{
                add(new Carousel(getString(R.string.open_leagues), true));
                add(new Carousel(getString(R.string.my_leagues), false));
                add(new Carousel(getString(R.string.pending_invitation), false));
            }}, R.color.color_blue, R.color.color_white, position -> {
                cvCarouselView.setActivePosition(position);
                vpViewPager.setCurrentItem(position);
            });

            // view pager
            leagueViewPagerAdapter = new LeagueViewPagerAdapter(getFragmentManager(), new ArrayList<BaseMvpFragment>() {{
                add(OpenLeagueFragment.newInstance());
                add(MyLeagueFragment.newInstance());
                add(PendingInvitationFragment.newInstance());
            }});
            vpViewPager.setOffscreenPageLimit(3);
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
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @NonNull
    @Override
    public ILeaguePresenter<ILeagueView> createPresenter() {
        return new LeagueDataPresenter(getAppComponent());
    }

    @OnClick(R.id.btnAdd)
    void onClickAdd() {
        AloneFragmentActivity.with(this)
                .parameters(SetUpLeagueFragment.newBundle(null, getString(R.string.leagues), LeagueDetailFragment.MY_LEAGUES))
                .start(SetUpLeagueFragment.class);
    }

    @Override
    public void openOpenLeague() {
        vpViewPager.setCurrentItem(OPEN_LEAGUE);
    }
}
