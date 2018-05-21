package com.football.fantasy;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.football.adapters.AccountViewPagerAdapter;
import com.football.common.activities.BaseAppCompatActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.fragments.account.signin.SignInFragment;
import com.football.fantasy.fragments.account.signup.SignUpFragment;

import java.util.ArrayList;

import butterknife.BindView;

public class AccountActivity extends BaseAppCompatActivity {
    private static final String TAG = AccountActivity.class.getSimpleName();

    @BindView(R.id.cvCarousel)
    CarouselView cvCarousel;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    @Override
    protected int getContentViewId() {
        return R.layout.account_activity;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        super.onCreate(savedInstanceState);

        // carousel
        cvCarousel.setAdapter(this, new ArrayList<Carousel>() {{
            add(new Carousel(getString(R.string.sign_in), true));
            add(new Carousel(getString(R.string.register), false));
        }}, R.color.color_blue, R.color.color_white, position -> {
            cvCarousel.setActivePosition(position);
            vpViewPager.setCurrentItem(position);
        });

        // view pager
        vpViewPager.setAdapter(new AccountViewPagerAdapter(getSupportFragmentManager(), new ArrayList<BaseMvpFragment>() {{
            add(SignInFragment.newInstance());
            add(SignUpFragment.newInstance());
        }}));
        vpViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                cvCarousel.setActivePosition(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public ActionBar getAppSupportActionBar() {
        return null;
    }

    @Override
    public Toolbar getToolBar() {
        return null;
    }
}
