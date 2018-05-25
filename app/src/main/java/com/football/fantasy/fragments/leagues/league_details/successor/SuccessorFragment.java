package com.football.fantasy.fragments.leagues.league_details.successor;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.adapters.SuccessorAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.Team;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.StreamSupport;

public class SuccessorFragment extends BaseMvpFragment<ISuccessorView, ISuccessorPresenter<ISuccessorView>> implements ISuccessorView {
    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvDone)
    ExtTextView tvDone;
    @BindView(R.id.rvRecyclerView)
    RecyclerView rvRecyclerView;

    List<Team> teams;
    SuccessorAdapter successorAdapter;

    @Override
    public int getResourceId() {
        return R.layout.successor_fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindButterKnife(view);
        initView();
    }

    void initView() {
        // only display button when at least a item selected
        tvDone.setVisibility(View.INVISIBLE);

        // adapter
        teams = new ArrayList<Team>() {{
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 1", "Clark Kent", true));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 2", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 3", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 4", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 5", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 6", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 7", "Clark Kent", false));
            add(new Team("https://dantricdn.com/zoom/327_245/2018/5/16/trump-1526427642048137816655.png", "My Supper Team 8", "Clark Kent", false));
        }};

        AtomicInteger currentChecked = new AtomicInteger(0);
        AtomicInteger nextChecked = new AtomicInteger(0);

        // adapter
        successorAdapter = new SuccessorAdapter(mActivity, teams, team -> {
            // precess data
            for (int i = 0; i < teams.size(); i++) {
                Team t = teams.get(i);
                // get current check
                if (t.isChecked()) currentChecked.set(i);

                // reset check
                t.setChecked(false);

                // next checked
                if (t.getTeamName().equalsIgnoreCase(team.getTeamName())) {
                    nextChecked.set(i);
                    t.setChecked(true);
                }
            }

            // notification
            successorAdapter.notifyItemChanged(currentChecked.get());
            successorAdapter.notifyItemChanged(nextChecked.get());

            // set visibility view
            tvDone.setVisibility(StreamSupport.stream(teams).filter(n -> n.isChecked()).count() > 0 ? View.VISIBLE : View.GONE);
        });

        rvRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        rvRecyclerView.setAdapter(successorAdapter);
    }

    @Override
    public ISuccessorPresenter<ISuccessorView> createPresenter() {
        return new SuccessorPresenter(getAppComponent());
    }

    @OnClick({R.id.tvCancel, R.id.tvDone})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvCancel:
                getActivity().finish();
                break;
            case R.id.tvDone:
                DialogUtils.messageBox(mActivity, getString(R.string.app_name), getString(R.string.leave_message),
                        getString(R.string.ok), (dialog, which) -> getActivity().finish());
                break;
        }
    }

    @Override
    public void initToolbar(@NonNull ActionBar supportActionBar) {
        super.initToolbar(supportActionBar);
        supportActionBar.hide();
    }
}
