package com.football.fantasy.fragments.leagues;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.football.adapters.LeagueAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.ActionLeagueFragment;
import com.football.fantasy.fragments.leagues.my_leagues.MyLeagueFragment;
import com.football.fantasy.fragments.leagues.open_leagues.OpenLeagueFragment;
import com.football.fantasy.fragments.leagues.pending_invitation.PendingInvitationFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueFragment extends BaseMvpFragment<ILeagueView, ILeaguePresenter<ILeagueView>> implements ILeagueView {
    public static LeagueFragment newInstance() {
        return new LeagueFragment();
    }

    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    LeagueAdapter leagueAdapter;

    @Override
    public int getResourceId() {
        return R.layout.league_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
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
        leagueAdapter = new LeagueAdapter(getFragmentManager(), new ArrayList<BaseMvpFragment>() {{
            add(OpenLeagueFragment.newInstance());
            add(MyLeagueFragment.newInstance());
            add(PendingInvitationFragment.newInstance());
        }});
        vpViewPager.setAdapter(leagueAdapter);
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
    public ILeaguePresenter<ILeagueView> createPresenter() {
        return new LeaguePresenter(getAppComponent());
    }

    @OnClick(R.id.btnAdd)
    void onClickAdd() {
        AloneFragmentActivity.with(this).start(ActionLeagueFragment.class);
    }
}
