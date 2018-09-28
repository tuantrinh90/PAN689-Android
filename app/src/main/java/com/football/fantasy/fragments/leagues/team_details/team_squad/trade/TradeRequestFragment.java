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
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal.TradeProposalFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.proposal_reveiew.ProposalReviewFragment;
import com.football.fantasy.fragments.leagues.team_details.team_squad.trade.request.RequestFragment;
import com.football.models.responses.TeamSquadResponse;
import com.football.models.responses.TradeResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class TradeRequestFragment extends BaseMvpFragment<ITradeRequestView, ITradeRequestPresenter<ITradeRequestView>> implements ITradeRequestView {

    private static final String KEY_TITLE = "TITLE";
    private static final String KEY_MY_TEAM_ID = "MY_TEAM_ID";
    private static final String KEY_TEAM_ID = "TEAM_ID";
    private static final String KEY_TEAM_NAME = "TEAM_NAME";
    private static final String KEY_TEAM_SQUAD = "TEAM_SQUAD";
    private static final String KEY_TAB_INDEX = "TAB_INDEX";
    private static final String KEY_LEAGUE_ID = "LEAGUE_ID";

    public static final int REQUEST_BY_YOU_INDEX = 0;
    public static final int REQUEST_TO_YOU_INDEX = 1;
    public static final int PROPOSAL_PREVIEW = 10;

    @BindView(R.id.tvNumberOfTradeLeft)
    ExtTextView tvNumberOfTradeLeft;
    @BindView(R.id.cvCarouselView)
    CarouselView cvCarouselView;
    @BindView(R.id.vpViewPager)
    ViewPager vpViewPager;

    private String title;
    private int myTeamId;
    private int teamId;
    private String teamName;
    private TeamSquadResponse teamSquad;
    private int tabIndex;
    private int leagueId;

    private int maxTradeRequest;
    private int currentTradeRequest;
    private int pendingTradeRequest;

    public static void startForNotification(Fragment fragment, String title, int myTeamId, int teamId, String teamName, TeamSquadResponse teamSquad, int tabIndex, int leagueId) {
        AloneFragmentActivity.with(fragment)
                .parameters(TradeRequestFragment.newBundle(title, myTeamId, teamId, teamName, teamSquad, tabIndex, leagueId))
                .start(TradeRequestFragment.class);
    }

    public static void start(Fragment fragment, String title, int myTeamId, int teamId, String teamName, TeamSquadResponse teamSquad, int tabIndex) {
        AloneFragmentActivity.with(fragment)
                .parameters(TradeRequestFragment.newBundle(title, myTeamId, teamId, teamName, teamSquad, tabIndex, -1))
                .start(TradeRequestFragment.class);
    }

    private static Bundle newBundle(String title, int myTeamId, int teamId, String teamName, TeamSquadResponse teamSquad, int tabIndex, int leagueId) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_TITLE, title);
        bundle.putInt(KEY_MY_TEAM_ID, myTeamId);
        bundle.putInt(KEY_TEAM_ID, teamId);
        bundle.putString(KEY_TEAM_NAME, teamName);
        bundle.putSerializable(KEY_TEAM_SQUAD, teamSquad);
        bundle.putInt(KEY_TAB_INDEX, tabIndex);
        bundle.putInt(KEY_LEAGUE_ID, leagueId);
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
        title = getArguments().getString(KEY_TITLE);
        myTeamId = getArguments().getInt(KEY_MY_TEAM_ID);
        teamId = getArguments().getInt(KEY_TEAM_ID);
        teamName = getArguments().getString(KEY_TEAM_NAME);
        teamSquad = (TeamSquadResponse) getArguments().getSerializable(KEY_TEAM_SQUAD);
        tabIndex = getArguments().getInt(KEY_TAB_INDEX);
        leagueId = getArguments().getInt(KEY_LEAGUE_ID);
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
    public ITradeRequestPresenter<ITradeRequestView> createPresenter() {
        return new TradeRequestPresenter(getAppComponent());
    }

    void initBackgroundToolbar() {
        Optional.from(mActivity.getToolBar()).doIfPresent(t -> t.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.colorPrimary)));
        Optional.from(mActivity.getTitleToolBar()).doIfPresent(t -> t.setTextColor(ContextCompat.getColor(mActivity, R.color.color_white)));
    }

    void initView() {
        initPages();

        if (tabIndex != -1) {
            // từ tabIndex >= 10 thì đó là action
            if (tabIndex >= 10) {
                if (tabIndex == PROPOSAL_PREVIEW) {
                    vpViewPager.setCurrentItem(REQUEST_TO_YOU_INDEX);
                    presenter.getTradeRequestsToYou(leagueId, teamId);
                }
            } else {
                vpViewPager.setCurrentItem(tabIndex);
            }
        }
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
        mAdapter.addFragment(RequestFragment.newInstance(
                RequestFragment.REQUEST_BY_YOU,
                teamSquad.getLeague().getId(),
                myTeamId).setChildFragment(true));
        mAdapter.addFragment(RequestFragment.newInstance(
                RequestFragment.REQUEST_TO_YOU,
                teamSquad.getLeague().getId(),
                myTeamId).setChildFragment(true));
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

    public void displayTradeRequestLeftDisplay(String tradeRequestLeftDisplay, int pendingTradeRequest, int currentTradeRequest, int maxTradeRequest) {
        tvNumberOfTradeLeft.setText(tradeRequestLeftDisplay);
        this.pendingTradeRequest = pendingTradeRequest;
        this.currentTradeRequest = currentTradeRequest;
        this.maxTradeRequest = maxTradeRequest;
    }

    @OnClick(R.id.ivMoreNumberOfTradeLeft)
    public void onInfoClick() {
        showMessage(getString(R.string.message_info_number_of_trade_left));
    }

    @OnClick(R.id.ivAdd)
    public void onAddClicked() {
        if (pendingTradeRequest >= maxTradeRequest) {
            showMessage(getString(R.string.pending_trade_request_max));
        } else if (maxTradeRequest - currentTradeRequest <= 0) {
            showMessage("currentTradeRequest <= 0");
        } else if (teamSquad.getMyTeam().getId() == teamId) {
            ChooseATeamFragment.start(getContext(), teamSquad.getLeague().getId(), teamSquad.getMyTeam().getId());
        } else {
            TradeProposalFragment.start(getContext(), teamSquad.getMyTeam().getId(), teamSquad.getMyTeam().getName(), teamId, teamName);
        }
    }

    @Override
    public void goProposalReview(TradeResponse trade) {
        ProposalReviewFragment.start(getContext(), getString(R.string.trade_request), trade, REQUEST_TO_YOU_INDEX);
    }
}
