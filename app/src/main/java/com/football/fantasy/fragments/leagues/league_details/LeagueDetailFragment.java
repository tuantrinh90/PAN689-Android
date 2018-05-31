package com.football.fantasy.fragments.leagues.league_details;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;
import android.widget.ImageView;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.util.DialogUtils;
import com.football.adapters.LeagueDetailViewPagerAdapter;
import com.football.common.Keys;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMainMvpFragment;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.BuildConfig;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.ActionLeagueFragment;
import com.football.fantasy.fragments.leagues.league_details.invite_friends.InviteFriendFragment;
import com.football.fantasy.fragments.leagues.league_details.league_info.LeagueInfoFragment;
import com.football.fantasy.fragments.leagues.league_details.successor.SuccessorFragment;
import com.football.fantasy.fragments.leagues.league_details.teams.TeamFragment;
import com.football.models.responses.LeagueResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class LeagueDetailFragment extends BaseMainMvpFragment<ILeagueDetailView, ILeagueDetailPresenter<ILeagueDetailView>> implements ILeagueDetailView {
    public static final String KEY_TITLE = "key_title";
    public static final String KEY_LEAGUE_ID = "key_league";

    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    String title;
    int leagueId;
    LeagueDetailViewPagerAdapter leagueDetailViewPagerAdapter;

    @Override
    public int getResourceId() {
        return R.layout.league_detail_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();

        presenter.getLeagueDetail(leagueId);
    }

    void getDataFromBundle() {
        Bundle bundle = mActivity.getIntent().getBundleExtra(Keys.ARGS);
        title = bundle.getString(KEY_TITLE, "");
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
    }

    void initView() {
        cvCarouselView.setTextAllCaps(false)
                .setFontPath(getString(R.string.font_display_heavy_italic))
                .setAdapter(mActivity, new ArrayList<Carousel>() {{
                    add(new Carousel(getString(R.string.league_information), true));
                    add(new Carousel(getString(R.string.teams), false));
                    add(new Carousel(getString(R.string.invite_friend), false));
                }}, R.color.color_blue, R.color.color_content, position -> {
                    cvCarouselView.setActivePosition(position);
                    vpViewPager.setCurrentItem(position);
                });
    }

    @NonNull
    @Override
    public ILeagueDetailPresenter<ILeagueDetailView> createPresenter() {
        return new LeagueDetailDataPresenter(getAppComponent());
    }

    @Override
    public String getTitleString() {
        return title;
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setHomeAsUpIndicator(R.drawable.ic_back_blue);
    }

    @OnClick(R.id.ivMenu)
    void onClickMenu() {
        ExtKeyValuePairDialogFragment.newInstance()
                .setValue("")
                .setExtKeyValuePairs(new ArrayList<ExtKeyValuePair>() {{
                    add(new ExtKeyValuePair("", getString(R.string.edit), ContextCompat.getColor(mActivity, R.color.color_blue)));
                    add(new ExtKeyValuePair("", getString(R.string.leave), ContextCompat.getColor(mActivity, R.color.color_blue)));
                    add(new ExtKeyValuePair("", getString(R.string.stop_league), ContextCompat.getColor(mActivity, R.color.color_red)));
                }})
                .setOnSelectedConsumer(extKeyValuePair -> {
                    // edit
                    if (extKeyValuePair.getValue().equalsIgnoreCase(getString(R.string.edit))) {
                        AloneFragmentActivity.with(LeagueDetailFragment.this).start(ActionLeagueFragment.class);
                    }

                    // leave
                    if (extKeyValuePair.getValue().equalsIgnoreCase(getString(R.string.leave))) {
                        AloneFragmentActivity.with(LeagueDetailFragment.this)
                                .start(SuccessorFragment.class);
                    }

                    // edit
                    if (extKeyValuePair.getValue().equalsIgnoreCase(getString(R.string.stop_league))) {
                        DialogUtils.confirmBox(mActivity, getString(R.string.app_name), getString(R.string.stop_league_message), getString(R.string.yes),
                                getString(R.string.no), (dialog, which) -> {
                                });
                    }
                }).show(getFragmentManager(), null);
    }

    @Override
    public void displayLeague(LeagueResponse league) {
        Optional.from(league).doIfPresent(l -> tvTitle.setText(l.getName()));

        leagueDetailViewPagerAdapter = new LeagueDetailViewPagerAdapter(getFragmentManager(),
                new ArrayList<BaseMvpFragment>() {{
                    add(LeagueInfoFragment.newInstance(league).setChildFragment(true));
                    add(TeamFragment.newInstance(BuildConfig.DEBUG ? 2 : league.getId()).setChildFragment(true)); // TODO: 5/31/2018 fake leagueId
                    add(InviteFriendFragment.newInstance().setChildFragment(true));
                }});
        vpViewPager.setAdapter(leagueDetailViewPagerAdapter);
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
