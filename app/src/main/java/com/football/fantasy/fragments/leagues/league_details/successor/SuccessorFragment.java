package com.football.fantasy.fragments.leagues.league_details.successor;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.interfaces.Optional;
import com.bon.logger.Logger;
import com.bon.util.DialogUtils;
import com.football.adapters.SuccessorAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.events.LeagueEvent;
import com.football.fantasy.R;
import com.football.models.responses.LeagueResponse;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.StreamSupport;

public class SuccessorFragment extends BaseMvpFragment<ISuccessorView, ISuccessorPresenter<ISuccessorView>> implements ISuccessorView {
    static final String TAG = SuccessorFragment.class.getSimpleName();
    public static final int REQUEST_CODE = 100;

    static final String KEY_LEAGUE = "key_leagues";

    public static Bundle newBundle(LeagueResponse leagueResponse) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_LEAGUE, leagueResponse);
        return bundle;
    }

    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvDone)
    ExtTextView tvDone;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<TeamResponse> teamResponses;
    SuccessorAdapter successorAdapter;
    TeamResponse teamResponse;
    LeagueResponse league;

    @Override
    public int getResourceId() {
        return R.layout.successor_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        getDataFromBundle();
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void getDataFromBundle() {
        league = (LeagueResponse) getArguments().getSerializable(KEY_LEAGUE);
    }

    void initView() {
        try {
            // only display button when at least a item selected
            tvDone.setVisibility(View.INVISIBLE);

            // adapter
            successorAdapter = new SuccessorAdapter(mActivity, teamResponses, teamResponse -> {
                // precess data
                for (int i = 0; i < successorAdapter.getItems().size(); i++) {
                    TeamResponse t = successorAdapter.getItems().get(i);
                    // reset check
                    t.setChecked(t.getName().equalsIgnoreCase(teamResponse.getName()));
                }

                // notification
                successorAdapter.notifyDataSetChanged(successorAdapter.getItems());

                // set visibility view
                tvDone.setVisibility(StreamSupport.stream(successorAdapter.getItems()).filter(n -> n.isChecked()).count() > 0 ? View.VISIBLE : View.GONE);
            });

            rvRecyclerView.init(mActivity, successorAdapter)
                    .setOnExtRefreshListener(() -> {
                        rvRecyclerView.clearItems();
                        getTeams();
                    });

            // load data
            getTeams();
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    private void getTeams() {
        rvRecyclerView.setMessage(getString(R.string.loading));
        presenter.getTeams(league.getId(), league.getOwner() ? league.getTeam().getId() : -1);
    }

    @Override
    public ISuccessorPresenter<ISuccessorView> createPresenter() {
        return new SuccessorPresenter(getAppComponent());
    }

    @OnClick({R.id.tvCancel, R.id.tvDone})
    public void onViewClicked(View view) {
        try {
            switch (view.getId()) {
                case R.id.tvCancel:
                    getActivity().setResult(Activity.RESULT_CANCELED);
                    getActivity().finish();
                    break;
                case R.id.tvDone:
                    if (successorAdapter.getItems() != null && successorAdapter.getItems().size() > 0) {
                        java8.util.Optional<TeamResponse> teamOptional = StreamSupport.stream(successorAdapter.getItems()).filter(n -> n.isChecked()).findAny();
                        if (teamOptional.isPresent()) teamResponse = teamOptional.get();
                    }

                    // leave league
                    DialogUtils.messageBox(mActivity, getString(R.string.app_name), getString(R.string.message_confirm_leave_leagues),
                            getString(R.string.ok), (dialog, which) -> presenter.leaveLeague(league.getId(), teamResponse != null ? teamResponse.getId() : 0));
                    break;
            }
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }

    @Override
    public void displayTeams(List<TeamResponse> teams) {
        try {
            tvDone.setVisibility(teams == null || teams.size() <= 0 ? View.VISIBLE : View.INVISIBLE);
            Optional.from(rvRecyclerView).doIfPresent(rv -> rv.addNewItems(teams));
        } catch (Exception e) {
            Logger.e(TAG, e);
        }
    }

    @Override
    public void onLeaveLeague() {
        bus.send(new LeagueEvent(LeagueEvent.ACTION_LEAVE, null));
        getActivity().finish();
    }

    @Override
    public void showLoadingPagingListView(boolean isLoading) {
        Optional.from(rvRecyclerView).doIfPresent(rv -> {
            if (isLoading) {
                rvRecyclerView.startLoading(true);
            } else {
                rvRecyclerView.stopLoading(true);
            }
        });
    }
}
