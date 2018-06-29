package com.football.fantasy.fragments.leagues.team_squad.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.interfaces.Optional;
import com.football.adapters.PagerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_squad.trade.record.RecordFragment;
import com.football.fantasy.fragments.leagues.team_squad.trade.transferring.TransferringFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class TradeFragment extends BaseMainMvpFragment<ITradeView, ITradePresenter<ITradeView>> implements ITradeView {

    private static final String KEY_TITLE = "TITLE";

    private String title;

    public static void start(Fragment fragment, String title) {
        AloneFragmentActivity.with(fragment)
                .parameters(TradeFragment.newBundle(title))
                .start(TradeFragment.class);
    }

    private static Bundle newBundle(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        return bundle;
    }

    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    private PagerAdapter mAdapter;

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initBackgroundToolbar();
        initView();
    }

    private void getDataFromBundle() {
        title = getArguments().getString(KEY_TITLE);
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_white);
    }

    @NonNull
    @Override
    public ITradePresenter<ITradeView> createPresenter() {
        return new TradeDataPresenter(getAppComponent());
    }

    void initBackgroundToolbar() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initView() {
        // carousel
        cvCarouselView.setAdapter(mActivity, new ArrayList<Carousel>() {{
            add(new Carousel(getString(R.string.transferring_player), true));
            add(new Carousel(getString(R.string.record), false));
        }}, R.color.color_blue, R.color.color_white, position -> {
            cvCarouselView.setActivePosition(position);
            vpViewPager.setCurrentItem(position);
        });

        // view pager
        mAdapter = new PagerAdapter(getChildFragmentManager());
        mAdapter.addFragment(TransferringFragment.newInstance());
        mAdapter.addFragment(RecordFragment.newInstance());
        vpViewPager.setAdapter(mAdapter);
        vpViewPager.setOffscreenPageLimit(2);
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