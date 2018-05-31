package com.football.fantasy.fragments.leagues.league_details.successor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.view.View;

import com.bon.customview.listview.ExtPagingListView;
import com.bon.customview.textview.ExtTextView;
import com.bon.util.DialogUtils;
import com.football.adapters.SuccessorAdapter;
import com.football.common.fragments.BaseMvpFragment;
import com.football.fantasy.R;
import com.football.models.responses.TeamResponse;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import java8.util.stream.StreamSupport;

public class SuccessorFragment extends BaseMvpFragment<ISuccessorView, ISuccessorPresenter<ISuccessorView>> implements ISuccessorView {
    @BindView(R.id.tvCancel)
    ExtTextView tvCancel;
    @BindView(R.id.tvDone)
    ExtTextView tvDone;
    @BindView(R.id.rvRecyclerView)
    ExtPagingListView rvRecyclerView;

    List<TeamResponse> teamResponses;
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
        successorAdapter = new SuccessorAdapter(mActivity, teamResponses, teamResponse -> {
            // precess data
            for (int i = 0; i < teamResponses.size(); i++) {
                TeamResponse t = teamResponses.get(i);
                // reset check
                t.setChecked(t.getName().equalsIgnoreCase(teamResponse.getName()));
            }

            // notification
            successorAdapter.notifyDataSetChanged(teamResponses);

            // set visibility view
            tvDone.setVisibility(StreamSupport.stream(teamResponses).filter(n -> n.isChecked()).count() > 0 ? View.VISIBLE : View.GONE);
        });
        rvRecyclerView.init(mActivity, successorAdapter);
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
