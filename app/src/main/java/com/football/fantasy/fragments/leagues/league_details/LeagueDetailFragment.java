package com.football.fantasy.fragments.leagues.league_details;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
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
import com.football.fantasy.BuildConfig;
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
import com.football.fantasy.fragments.leagues.player_pool.PlayerPoolFragment;
import com.football.fantasy.fragments.leagues.team_details.gameplay_option.GameplayFragment;
import com.football.fantasy.fragments.leagues.team_details.gameplay_option.transferring.TransferringFragment;
import com.football.fantasy.fragments.leagues.your_team.YourTeamFragment;
import com.football.models.responses.LeagueResponse;
import com.football.utilities.AppUtilities;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.football.fantasy.fragments.leagues.league_details.trade_review.TradeReviewFragment.INDEX_RESULTS;
import static com.football.models.responses.LeagueResponse.FINISHED;
import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_DRAFT;
import static com.football.models.responses.LeagueResponse.GAMEPLAY_OPTION_TRANSFER;
import static com.football.models.responses.LeagueResponse.LEAGUE_TYPE_OPEN;
import static com.football.models.responses.LeagueResponse.ON_GOING;
import static com.football.models.responses.LeagueResponse.WAITING_FOR_START;
import static com.football.services.NotificationKey.BEFORE_START_TIME_2H;
import static com.football.services.NotificationKey.BEFORE_TEAM_SETUP_TIME_1H;
import static com.football.services.NotificationKey.BEFORE_TEAM_SETUP_TIME_2H;
import static com.football.services.NotificationKey.BEFORE_TRANSFER_DEADLINE_2H;
import static com.football.services.NotificationKey.CHANGE_LEAGUE_NAME;
import static com.football.services.NotificationKey.CHANGE_OWNER_LEAGUE;
import static com.football.services.NotificationKey.CHANGE_TEAM_NAME;
import static com.football.services.NotificationKey.COMPLETE_SETUP_TEAM;
import static com.football.services.NotificationKey.EDIT_LEAGUE;
import static com.football.services.NotificationKey.FULL_TEAM;
import static com.football.services.NotificationKey.LEAGUE_FINISH;
import static com.football.services.NotificationKey.LEAGUE_FINISH_FOR_CHAMPION;
import static com.football.services.NotificationKey.RANDOM_TEAM;
import static com.football.services.NotificationKey.TEAM_SETUP_TIME;
import static com.football.services.NotificationKey.TRADE_PROPOSAL_APPROVED;
import static com.football.services.NotificationKey.TRANSACTION_RESULT;
import static com.football.services.NotificationKey.TWO_HOURS_TO_REVIEW;
import static com.football.services.NotificationKey.USER_ACCEPT_INVITE;
import static com.football.services.NotificationKey.USER_JOINED_LEAGUE;
import static com.football.services.NotificationKey.USER_LEFT_LEAGUE;
import static com.football.services.NotificationKey.USER_REJECT_INVITE;
import static com.football.services.NotificationKey.USER_TRANSACTION_RESULT;

public class LeagueDetailFragment extends BaseMvpFragment<ILeagueDetailView, ILeagueDetailPresenter<ILeagueDetailView>> implements ILeagueDetailView {

    private static final String PERMISSION_READ_EXTERNAL_STORAGE = android.Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final String PERMISSION_WRITE_EXTERNAL_STORAGE = android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

    private static final String TAG = LeagueDetailFragment.class.getSimpleName();
    private static final String KEY_TITLE = "key_title";
    private static final String KEY_LEAGUE_ID = "key_league";
    private static final String KEY_LEAGUE_TYPE = "key_league_type";
    private static final String KEY_INVITATION_ID = "INVITATION_ID";
    private static final String KEY_OPEN_ROUND = "OPEN_RESULT";
    private static final String KEY_ACTION = "ACTION";
    private static final String KEY_TRADE_PREVIEW_ID = "TRADE_PREVIEW_ID";

    public static final int INFORMATION_INDEX = 0;
    public static final int TEAMS_INDEX = 1;
    public static final int RANKING_INDEX = 2;
    public static final int RESULTS_INDEX = 3;
    public static final int TRADE_REVIEW_INDEX = 4;

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

    private String title;
    private int leagueId;
    private String leagueType;
    private int invitationId;
    private int openRound;
    private String action;
    private int tradePreviewId;

    private LeagueResponse league;
    private LeagueDetailViewPagerAdapter adapter;
    private List<ExtKeyValuePair> valuePairs = new ArrayList<>();

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

    public static Bundle newBundleForNotification(String title, int leagueId, String action) {
        Bundle bundle = newBundle(title, leagueId, MY_LEAGUES);
        bundle.putString(KEY_ACTION, action);
        return bundle;
    }

    public static Bundle newBundleForNotificationTradePreview(String title, int leagueId, String action, int tradePreviewId) {
        Bundle bundle = newBundle(title, leagueId, MY_LEAGUES);
        bundle.putString(KEY_ACTION, action);
        bundle.putInt(KEY_TRADE_PREVIEW_ID, tradePreviewId);
        return bundle;
    }

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

        // request permission
        if (BuildConfig.DEBUG) {
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(mActivity, PERMISSION_READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(mActivity,
                        new String[]{PERMISSION_READ_EXTERNAL_STORAGE, PERMISSION_WRITE_EXTERNAL_STORAGE},
                        1000);
            }
        }
    }

    void getDataFromBundle() {
        if (getArguments() == null) return;
        Bundle bundle = getArguments();
        title = bundle.getString(KEY_TITLE, "");
        leagueId = bundle.getInt(KEY_LEAGUE_ID);
        leagueType = bundle.getString(KEY_LEAGUE_TYPE, "");
        invitationId = bundle.getInt(KEY_INVITATION_ID);
        openRound = bundle.getInt(KEY_OPEN_ROUND);
        action = bundle.getString(KEY_ACTION);
        tradePreviewId = bundle.getInt(KEY_TRADE_PREVIEW_ID);
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
        onEvent(LeagueEvent.class, event -> {
            switch (event.getAction()) {
                case LeagueEvent.ACTION_UPDATE:
                    if (event.getLeague() != null) {
                        LeagueDetailFragment.this.league = event.getLeague();
                        displayLeague(event.getLeague());
                        if (adapter.getItem(0) instanceof LeagueInfoFragment) {
                            ((LeagueInfoFragment) adapter.getItem(0)).displayLeague(event.getLeague());
                        }
                    }
                    break;

                case LeagueEvent.ACTION_LEAVE:
                    mActivity.finish();
                    break;
            }
        });
        onEvent(StartLeagueEvent.class, event -> {
            LeagueDetailFragment.this.leagueId = event.getLeague().getId();
            LeagueDetailFragment.this.leagueType = event.getLeague().getLeagueType();
            LeagueDetailFragment.this.league = event.getLeague();

            presenter.getLeagueDetail(leagueId);
        });
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

        ivMenu.setVisibility(View.GONE);

        valuePairs.clear();
        // owner
        if (league.getOwner()) {
            valuePairs.add(new ExtKeyValuePair("", getString(R.string.edit), ContextCompat.getColor(mActivity, R.color.color_blue)));
        }

        if (AppUtilities.isSetupTime(league)) {
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
        if (!league.getOwner() && !AppUtilities.isSetupTime(league)) {
            ivMenu.setVisibility(View.GONE);
        }
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

        if (league.equalsStatus(WAITING_FOR_START)) {
            // only display invite with open leagues or owner
            if (league.getOwner() || (league.getLeagueType().equalsIgnoreCase(LEAGUE_TYPE_OPEN) && league.getIsJoined())) {
                carousels.add(new Carousel(getString(R.string.invite_friend), false));
                mvpFragments.add(InviteFriendFragment.newInstance(league, leagueType).setChildFragment(true));
            }
        } else if (league.equalsStatus(ON_GOING)) {
            carousels.add(new Carousel(getString(R.string.ranking), false));
            carousels.add(new Carousel(getString(R.string.results), false));

            mvpFragments.add(RankingFragment.newInstance(league).setChildFragment(true));
            mvpFragments.add(ResultsFragment.newInstance(league).setChildFragment(true));

            if (league.equalsGameplay(GAMEPLAY_OPTION_DRAFT)) {
                carousels.add(new Carousel(getString(R.string.trade_review), false));
                mvpFragments.add(TradeReviewFragment.newInstance(league).setChildFragment(true));
            }
        } else if (league.equalsStatus(FINISHED)) {
            carousels.add(new Carousel(getString(R.string.ranking), false));
            carousels.add(new Carousel(getString(R.string.results), false));

            mvpFragments.add(RankingFragment.newInstance(league).setChildFragment(true));
            mvpFragments.add(ResultsFragment.newInstance(league).setChildFragment(true));

            if (league.equalsGameplay(GAMEPLAY_OPTION_DRAFT)) {
                carousels.add(new Carousel(getString(R.string.trade_review), false));
                mvpFragments.add(TradeReviewFragment.newInstance(league).setChildFragment(true));
            }
        }

        // carousel view
        cvCarouselView.setTextAllCaps(false)
                .setFontPath(getString(R.string.font_display_heavy_italic))
                .setAdapter(mActivity, carousels, R.color.color_blue, R.color.color_content, position -> {
                    cvCarouselView.setActivePosition(position);
                    vpViewPager.setCurrentItem(position);
                });
        // adapter
        adapter = new LeagueDetailViewPagerAdapter(getChildFragmentManager(), mvpFragments);
        vpViewPager.setAdapter(adapter);
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

    /**
     * goLineup: dành cho trường hợp Notification bắn vào màn lineup mà thời gian đang ở DraftTime(cũng vào lineup)
     */
    @Override
    public void handleActionNotification(boolean goLineup) {
        if (!TextUtils.isEmpty(action)) {
            switch (action) {
                // League detail
                case USER_LEFT_LEAGUE:
                case BEFORE_START_TIME_2H:
                case EDIT_LEAGUE:
                case CHANGE_LEAGUE_NAME:
                case CHANGE_TEAM_NAME: // action này chưa hiểu lắm
                case BEFORE_TRANSFER_DEADLINE_2H:
                    // do nothing
                    break;

                // Tab team
                case USER_JOINED_LEAGUE:
                case USER_ACCEPT_INVITE:
                case USER_REJECT_INVITE:
                case CHANGE_OWNER_LEAGUE:
                case FULL_TEAM:
                    vpViewPager.setCurrentItem(TEAMS_INDEX);
                    break;

                case LEAGUE_FINISH:
                case LEAGUE_FINISH_FOR_CHAMPION:
                    vpViewPager.setCurrentItem(RANKING_INDEX);
                    break;

                case TRANSACTION_RESULT:
                case TRADE_PROPOSAL_APPROVED:
                    vpViewPager.setCurrentItem(TRADE_REVIEW_INDEX);
                    break;

                case USER_TRANSACTION_RESULT:
                    vpViewPager.setCurrentItem(TRADE_REVIEW_INDEX);
                    new Handler().postDelayed(() -> {
                        if (adapter.getItem(TRADE_REVIEW_INDEX) instanceof TradeReviewFragment) {
                            ((TradeReviewFragment) adapter.getItem(TRADE_REVIEW_INDEX)).openFragment(INDEX_RESULTS);
                        }
                    }, 150);

                    break;

                case TWO_HOURS_TO_REVIEW:
                    vpViewPager.setCurrentItem(TRADE_REVIEW_INDEX);
                    new Handler().postDelayed(() -> {
                        if (adapter.getItem(TRADE_REVIEW_INDEX) instanceof TradeReviewFragment) {
                            ((TradeReviewFragment) adapter.getItem(TRADE_REVIEW_INDEX)).openTradeProposalReview(tradePreviewId);
                        }
                    }, 150);
                    break;

                // Setup team - screen Lineup
                case TEAM_SETUP_TIME:
                case BEFORE_TEAM_SETUP_TIME_2H:
                case RANDOM_TEAM:
                case COMPLETE_SETUP_TEAM:   // Setup team - tab team list
                    new Handler().postDelayed(() -> {
                        if (!goLineup && adapter != null) {
                            ((LeagueInfoFragment) adapter.getItem(INFORMATION_INDEX)).openSetupTeam();
                        }
                    }, 150);
                    break;

                case BEFORE_TEAM_SETUP_TIME_1H:
                    AloneFragmentActivity.with(LeagueDetailFragment.this)
                            .parameters(SetUpLeagueFragment.newBundle(league, getString(R.string.league_details)))
                            .start(SetUpLeagueFragment.class);
                    break;
            }

        } else if (openRound > 0) {
            vpViewPager.setCurrentItem(RESULTS_INDEX);
            ((ResultsFragment) adapter.getItem(RESULTS_INDEX)).displayRound(openRound);
        }
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
    public void goCreateTeam() {
        AloneFragmentActivity.with(mActivity)
                .parameters(SetupTeamFragment.newBundle(
                        null,
                        leagueId,
                        mActivity.getTitleToolBar().getText().toString()))
                .start(SetupTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void goLineup() {
        AloneFragmentActivity.with(this)
                .parameters(YourTeamFragment.newBundle(league))
                .start(YourTeamFragment.class);
        mActivity.finish();
    }

    @Override
    public void stopOrLeaveLeagueSuccess() {
        bus.send(new StopLeagueEvent(leagueId));
        getActivity().finish();
    }

    @Override
    public void handleLessThan18Players(ArrayList<Integer> playerIds, long value) {
        showMessage(
                league.equalsGameplay(GAMEPLAY_OPTION_TRANSFER) ?
                        getString(R.string.message_players_has_left_transfer, AppUtilities.getMoney(value)) :
                        getString(R.string.message_players_has_left_draft),
                R.string.ok,
                aVoid -> {
                    PlayerPoolFragment.start(
                            this,
                            getString(R.string.league_details),
                            getString(R.string.player_pool),
                            playerIds,
                            league.getSeasonId(),
                            league.getTeam().getId(),
                            league.getId(),
                            league.getGameplayOption());
                });
    }

    @Override
    public void handleMoreThan18Players(int numberPlayer) {
        showMessage(
                getString(R.string.message_trade_team_more_than_18_players),
                R.string.ok,
                aVoid -> {
                    GameplayFragment.start(this,
                            getString(R.string.team_details),
                            league.getTeam(),
                            league,
                            TransferringFragment.ACTION_ONLY_REMOVE,
                            numberPlayer);
                });
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
