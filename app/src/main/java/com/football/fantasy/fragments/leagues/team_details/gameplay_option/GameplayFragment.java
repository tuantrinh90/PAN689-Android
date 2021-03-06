package com.football.fantasy.fragments.leagues.team_details.gameplay_option;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.interfaces.Optional;
import com.football.adapters.StatePagerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.team_details.gameplay_option.record.RecordFragment;
import com.football.fantasy.fragments.leagues.team_details.gameplay_option.transferring.TransferringFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.ArrayList;

import butterknife.BindView;

import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;

public class GameplayFragment extends BaseMvpFragment<IGameplayView, IGameplayPresenter<IGameplayView>> implements IGameplayView {

    private static final String KEY_TEAM = "TEAM";
    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_LEAGUE = "LEAGUE";
    private static final String KEY_ACTION = "ACTION";
    private static final String KEY_NUMBER_PLAYER_TO_REMOVE = "NUMBER_PLAYER_TO_REMOVE";

    private TeamResponse team;
    private String title;
    private LeagueResponse league;
    private String action;
    private int numberPlayerToRemove;

    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    public static void start(Fragment fragment, String title, TeamResponse team, LeagueResponse league, String action, int numberPlayer) {
        AloneFragmentActivity.with(fragment)
                .parameters(GameplayFragment.newBundle(title, team, league, action, numberPlayer))
                .start(GameplayFragment.class);
    }

    private static Bundle newBundle(String title, TeamResponse team, LeagueResponse league, String action,  int numberPlayerToRemove) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_TEAM, team);
        bundle.putString(KEY_TITLE, title);
        bundle.putSerializable(KEY_LEAGUE, league);
        bundle.putString(KEY_ACTION, action);
        bundle.putInt(KEY_NUMBER_PLAYER_TO_REMOVE, numberPlayerToRemove);
        return bundle;
    }

    @Override
    public int getResourceId() {
        return R.layout.team_squad_gameplay_option_fragment;
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
        action = getArguments().getString(KEY_ACTION);
        numberPlayerToRemove = getArguments().getInt(KEY_NUMBER_PLAYER_TO_REMOVE);
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
    public IGameplayPresenter<IGameplayView> createPresenter() {
        return new GameplayPresenter(getAppComponent());
    }

    void initBackgroundToolbar() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initView() {
        initPages(league.equalsGameplay(GAMEPLAY_OPTION_TRANSFER));
    }

    private void initPages(boolean isTransferMode) {
        // carousel
        cvCarouselView.setAdapter(mActivity, new ArrayList<Carousel>() {{
            add(new Carousel(getString(isTransferMode ? R.string.transferring_player : R.string.waiving_player), true));
            add(new Carousel(getString(R.string.record), false));
        }}, R.color.color_blue, R.color.color_white, position -> {
            cvCarouselView.setActivePosition(position);
            vpViewPager.setCurrentItem(position);
        });

        // view pager
        StatePagerAdapter mAdapter = new StatePagerAdapter(getChildFragmentManager());
        mAdapter.addFragment(TransferringFragment.newInstance(team, league, action,numberPlayerToRemove).setChildFragment(true));
        mAdapter.addFragment(RecordFragment.newInstance(team, league).setChildFragment(true));
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

}
