package com.football.fantasy.fragments.leagues.league_details;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.keyvaluepair.ExtKeyValuePair;
import com.bon.customview.keyvaluepair.ExtKeyValuePairDialogFragment;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.DialogUtils;
import com.football.adapters.LeagueDetailViewPagerAdapter;
import com.football.common.activities.AloneFragmentActivity;
import com.football.common.fragments.BaseMvpFragment;
import com.football.customizes.carousels.Carousel;
import com.football.customizes.carousels.CarouselView;
import com.football.events.LeagueEvent;
import com.football.events.StartLeagueEvent;
import com.football.events.StopLeagueEvent;
import com.football.fantasy.R;
import com.football.fantasy.fragments.leagues.action.setup_leagues.SetUpLeagueFragment;
import com.football.fantasy.fragments.leagues.action.setup_teams.SetupTeamFragment;
import com.football.fantasy.fragments.leagues.league_details.invite_friends.InviteFriendFragment;
import com.football.fantasy.fragments.leagues.league_details.league_info.LeagueInfoFragment;
import com.football.fantasy.fragments.leagues.league_details.ranking.RankingFragment;
import com.football.fantasy.fragments.leagues.league_details.results.ResultsFragment;
import com.football.fantasy.fragments.leagues.league_details.successor.SuccessorFragment;
import com.football.fantasy.fragments.leagues.league_details.teams.TeamFragment;
import com.football.fantasy.fragments.leagues.league_details.trade_review.TradeReviewFragment;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;
import com.football.utilities.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.observers.DisposableObserver;

import static com.football.models.responses.LeagueResponse.LEAGUE_TYPE_OPEN;

public class LeagueDetailFragment extends BaseMvpFragment<ILeagueDetailView, ILeagueDetailPresenter<ILeagueDetailView>> implements ILeagueDetailView {
    private static final String TAG = LeagueDetailFragment.class.getSimpleName();
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_LEAGUE_ID = "key_league";
    private static final String KEY_LEAGUE_TYPE = "key_league_type";
    private static final String KEY_INVITATION_ID = "INVITATION_ID";
    private static final String KEY_OPEN_ROUND = "OPEN_RESULT";
    private static final String KEY_FRAGMENT_INDEX = "FRAGMENT_INDEX";

    public static final int TEAM_FRAGMENT_INDEX = 1;
    public static final int RESULT_FRAGMENT_INDEX = 3;

    public static final String OPEN_LEAGUES = "open_leagues";
    public static final String MY_LEAGUES = "my_leagues";
    public static final String PENDING_LEAGUES = "pending_leagues";


    @BindView(R.id.tvTitle)
    ExtTextView tvTitle;
    @BindView(R.id.ivMenu)
    View ivMenu;
    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    public static Bundle newBundle(String title, int leagueId, String leagueType) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
        bundle.putString(KEY_LEAGUE_TYPE, leagueType);
        return bundle;
    }

    public static Bundle newBundleForInvitation(String title, int leagueId, String leagueType, int invitationId) {
        Bundle bundle = newBundle(title, leagueId, leagueType);
        bundle.putInt(KEY_INVITATION_ID, invitationId);
        return bundle;
    }

    public static Bundle newBundle(String title, int leagueId, int round) {
        Bundle bundle = newBundle(title, leagueId, MY_LEAGUES);
        bundle.putInt(KEY_OPEN_ROUND, round);
        return bundle;
    }

    public static Bundle newBundleForNotification(String title, int leagueId, int index) {
        Bundle bundle = newBundle(title, leagueId, MY_LEAGUES);
        bundle.putInt(KEY_FRAGMENT_INDEX, index);
        return bundle;
    }

    private String title;
    private int leagueId;
    private String leagueType;
    private int invitationId;
    private int openRound;
    private int fragmentIndex;

    private LeagueResponse league;
    private LeagueDetailViewPagerAdapter leagueDetailViewPagerAdapter;
    private List<ExtKeyValuePair> valuePairs = new ArrayList<>();

    @Override
    public int getResourceId() {
        return R.layout.league_detail_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        registerEvent();
        presenter.getLeagueDetail(leagueId);
    }

    void getDataFromBundle() {
        if (getArguments() == null) return;
        Bundle bundle = getArguments();
        title = bundle.getString(KEY_TITLE, "");
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
        leagueType = bundle.getString(KEY_LEAGUE_TYPE, "");
        invitationId = bundle.getInt(KEY_INVITATION_ID);
        openRound = bundle.getInt(KEY_OPEN_ROUND);
        fragmentIndex = bundle.getInt(KEY_FRAGMENT_INDEX, -1);
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

    void registerEvent() {
        try {
            // action add click on PlayerList
            mCompositeDisposable.add(bus.ofType(LeagueEvent.class)
                    .subscribeWith(new DisposableObserver<LeagueEvent>() {
                        @Override
                        public void onNext(LeagueEvent event) {
                            switch (event.getAction()) {
                                case LeagueEvent.ACTION_UPDATE:
                                    if (event.getLeague() != null) {
                                        LeagueDetailFragment.this.league = event.getLeague();
                                        displayLeague(event.getLeague());
                                        if (leagueDetailViewPagerAdapter.getItem(0) instanceof LeagueInfoFragment) {
                                            ((LeagueInfoFragment) leagueDetailViewPagerAdapter.getItem(0)).displayLeague(event.getLeague());
                                        }
                                    }
                                    break;

                                case LeagueEvent.ACTION_LEAVE:
                                    mActivity.finish();
                                    break;
                            }
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));

            // start league
            mCompositeDisposable.add(bus.ofType(StartLeagueEvent.class)
                    .subscribeWith(new DisposableObserver<StartLeagueEvent>() {
                        @Override
                        public void onNext(StartLeagueEvent event) {
                            LeagueDetailFragment.this.leagueId = event.getLeague().getId();
                            LeagueDetailFragment.this.leagueType = event.getLeague().getLeagueType();
                            LeagueDetailFragment.this.league = event.getLeague();

                            presenter.getLeagueDetail(leagueId);
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }));

        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @OnClick(R.id.ivMenu)
    void onClickMenu() {
        try {
            if (league == null) return;

            ExtKeyValuePairDialogFragment.newInstance()
                    .setValue("")
                    .setExtKeyValuePairs(valuePairs)
                    .setOnSelectedConsumer(extKeyValuePair -> {
                        try {
                            // edit
                            if (extKeyValuePair.getValue().equalsIgnoreCase(getString(R.string.edit))) {
                                AloneFragmentActivity.with(LeagueDetailFragment.this)
                                        .parameters(SetUpLeagueFragment.newBundle(league, title))
                                        .start(SetUpLeagueFragment.class);
                            }

                            // leave
                            if (extKeyValuePair.getValue().equalsIgnoreCase(getString(R.string.leave))) {
                                if (AppUtilities.isOwner(getContext(), league.getUserId())) {
                                    if (league.getCurrentNumberOfUser() > 1) {
                                        AloneFragmentActivity.with(LeagueDetailFragment.this)
                                                .parameters(SuccessorFragment.newBundle(league))
                                                .forResult(SuccessorFragment.REQUEST_CODE)
                                                .start(SuccessorFragment.class);
                                    } else {
                                        showMessage(R.string.message_confirm_leave_leagues, R.string.yes, R.string.no,
                                                aVoid -> presenter.stopLeague(leagueId), null);
                                    }
                                } else {
                                    showMessage(R.string.message_confirm_leave_leagues, R.string.yes, R.string.no,
                                            aVoid -> presenter.leaveLeague(leagueId), null);
                                }
                            }

                            // stop league
                            if (extKeyValuePair.getValue().equalsIgnoreCase(getString(R.string.stop_league))) {
                                DialogUtils.confirmBox(mActivity, getString(R.string.app_name), getString(R.string.stop_league_message), getString(R.string.yes),
                                        getString(R.string.no), (dialog, which) -> presenter.stopLeague(leagueId));
                            }
                        } catch (Exception e) {
                            Logger.e(TAG, e);
                        }
                    }).show(getFragmentManager(), null);
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void displayMenu(LeagueResponse league) {
        this.league = league;
        // auto create team if owner or joined
        autoCheckTeamExist(league);

        ivMenu.setVisibility(View.GONE);

        valuePairs.clear();
        // owner
        if (league.getOwner()) {
            valuePairs.add(new ExtKeyValuePair("", getString(R.string.edit), ContextCompat.getColor(mActivity, R.color.color_blue)));
        }

        if (league.getStatus() == LeagueResponse.WAITING_FOR_START) {
            // my leagues or owner
            if (league.getIsJoined() || league.getOwner()) {
                valuePairs.add(new ExtKeyValuePair("", getString(R.string.leave), ContextCompat.getColor(mActivity, R.color.color_blue)));
            }

            // only owner league has stop leagues
            if (league.getOwner()) {
                valuePairs.add(new ExtKeyValuePair("", getString(R.string.stop_league), ContextCompat.getColor(mActivity, R.color.color_red)));
            }
        }

        // show/hide menu
        ivMenu.setVisibility(valuePairs.size() > 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void displayLeaguePager(LeagueResponse response) {
        // update league
        List<BaseMvpFragment> mvpFragments = new ArrayList<>();
        List<Carousel> carousels = new ArrayList<>();

        carousels.add(new Carousel(getString(R.string.league_information), true));
        carousels.add(new Carousel(getString(R.string.teams), false));

        // view pager
        mvpFragments.add(LeagueInfoFragment.newInstance(league, leagueType, invitationId).setChildFragment(true));
        mvpFragments.add(TeamFragment.newInstance(league, leagueType).setChildFragment(true));

        if (league.getStatus() == LeagueResponse.WAITING_FOR_START) {
            // only display invite with open leagues or owner
            if (league.getOwner() || (league.getLeagueType().equalsIgnoreCase(LEAGUE_TYPE_OPEN) && league.getIsJoined())) {
                carousels.add(new Carousel(getString(R.string.invite_friend), false));
                mvpFragments.add(InviteFriendFragment.newInstance(league, leagueType, league.getStatus() != LeagueResponse.WAITING_FOR_START).setChildFragment(true));
            }
        } else if (league.getStatus() == LeagueResponse.ON_GOING) {
            carousels.add(new Carousel(getString(R.string.ranking), false));
            carousels.add(new Carousel(getString(R.string.results), false));
            carousels.add(new Carousel(getString(R.string.trade_review), false));

            mvpFragments.add(RankingFragment.newInstance(league).setChildFragment(true));
            mvpFragments.add(ResultsFragment.newInstance(league).setChildFragment(true));
            mvpFragments.add(TradeReviewFragment.newInstance(league).setChildFragment(true));
        } else if (league.getStatus() == LeagueResponse.FINISHED) {
            carousels.add(new Carousel(getString(R.string.ranking), false));
            carousels.add(new Carousel(getString(R.string.results), false));

            mvpFragments.add(RankingFragment.newInstance(league).setChildFragment(true));
            mvpFragments.add(ResultsFragment.newInstance(league).setChildFragment(true));
        }

        // carousel view
        cvCarouselView.setTextAllCaps(false)
                .setFontPath(getString(R.string.font_display_heavy_italic))
                .setAdapter(mActivity, carousels, R.color.color_blue, R.color.color_content, position -> {
                    cvCarouselView.setActivePosition(position);
                    vpViewPager.setCurrentItem(position);
                });
        // adapter
        leagueDetailViewPagerAdapter = new LeagueDetailViewPagerAdapter(getFragmentManager(), mvpFragments);
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
        try {
            if (fragmentIndex != -1) {
                vpViewPager.setCurrentItem(fragmentIndex);

            } else if (openRound > 0) {
                vpViewPager.setCurrentItem(RESULT_FRAGMENT_INDEX);
                ((ResultsFragment) leagueDetailViewPagerAdapter.getItem(RESULT_FRAGMENT_INDEX)).displayRound(openRound);
            }
        } catch (Exception e) {
        }
    }

    void autoCheckTeamExist(LeagueResponse league) {
        if (league.getOwner() || league.getIsJoined()) {
            TeamResponse team = league.getTeam();
            if (team == null) {
                createTeam();
            }
        }
    }

    void createTeam() {
        AloneFragmentActivity.with(mActivity)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        leagueId,
                        mActivity.getTitleToolBar().getText().toString()))
                .start(SetupTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void displayLeague(LeagueResponse league) {
        try {
            // load info
            Optional.from(tvTitle).doIfPresent(t -> t.setText(league.getName()));

        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void stopOrLeaveLeagueSuccess() {
        bus.send(new StopLeagueEvent(leagueId));
        getActivity().finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == SuccessorFragment.REQUEST_CODE) {
            bus.send(new StopLeagueEvent(leagueId));
            getActivity().finish();
        }
    }
}
