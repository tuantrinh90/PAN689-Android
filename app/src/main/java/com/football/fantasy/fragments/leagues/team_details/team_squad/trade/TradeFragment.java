package com.football.fantasy.fragments.leagues.team_details.team_squad.trade;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.football.adapters.StatePagerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.choose_a_team.ChooseATeamFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request.RequestFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TradeFragment extends BaseMvpFragment<ITradeView, ITradePresenter<ITradeView>> implements ITradeView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_LEAGUE = "LEAGUE";

    @BindView(R.id.tvNumberOfTradeLeft)
    ExtTextView tvNumberOfTradeLeft;
    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    private TeamResponse team;
    private String title;
    private LeagueResponse league;

    public static void start(Fragment fragment, String title, TeamResponse team, LeagueResponse league) {
        AloneFragmentActivity.with(fragment)
                .parameters(TradeFragment.newBundle(title, team, league))
                .start(TradeFragment.class);
    }

    private static Bundle newBundle(String title, TeamResponse team, LeagueResponse league) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_LEAGUE, league);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_trade_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);

        initView();
    }

    private void getDataFromBundle() {
        team = (TeamResponse) getArguments().getSerializable(KEY_TEAM);
        title = getArguments().getString(KEY_TITLE);
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
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
        initBackgroundToolbar();
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
        tvNumberOfTradeLeft.setText(team.getTradeRequestLeftDisplay());
        initPages();
    }

    private void initPages() {
        // carousel
        cvCarouselView.setAdapter(mActivity, new ArrayList<Carousel>() {{
            add(new Carousel(getString(R.string.request_by_you), true));
            add(new Carousel(getString(R.string.request_to_you), false));
        }}, R.color.color_blue, R.color.color_white, position -> {
            cvCarouselView.setActivePosition(position);
            vpViewPager.setCurrentItem(position);
        });

        // view pager
        StatePagerAdapter mAdapter = new StatePagerAdapter(getChildFragmentManager());
        mAdapter.addFragment(RequestFragment.newInstance(RequestFragment.REQUEST_FROM, league, team.getId()).setChildFragment(true));
        mAdapter.addFragment(RequestFragment.newInstance(RequestFragment.REQUEST_TO, league, team.getId()).setChildFragment(true));
        vpViewPager.setAdapter(mAdapter);
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

    @OnClick(R.id.ivMoreNumberOfTradeLeft)
    public void onInfoClick() {
        showMessage(getString(R.string.message_info_number_of_trade_left));
    }

    @OnClick(R.id.ivAdd)
    public void onAddClicked() {
        ChooseATeamFragment.start(getContext(), league.getId(), team.getId());
    }
}
